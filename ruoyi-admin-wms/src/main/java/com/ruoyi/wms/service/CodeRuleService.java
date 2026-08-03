package com.ruoyi.wms.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.wms.domain.bo.CodeRuleBo;
import com.ruoyi.wms.domain.entity.CodeRule;
import com.ruoyi.wms.domain.vo.CodeRuleVo;
import com.ruoyi.wms.mapper.CodeRuleMapper;
import com.ruoyi.wms.mapper.ItemCodeSeqMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 编码规则服务
 */
@Service
@RequiredArgsConstructor
public class CodeRuleService {

    private final CodeRuleMapper codeRuleMapper;
    private final ItemCodeSeqMapper itemCodeSeqMapper;

    // ==================== CRUD ====================

    public List<CodeRuleVo> queryList() {
        List<CodeRule> list = codeRuleMapper.selectList(
            Wrappers.<CodeRule>lambdaQuery().orderByAsc(CodeRule::getSortOrder).orderByAsc(CodeRule::getRuleType));
        return MapstructUtils.convert(list, CodeRuleVo.class);
    }

    public CodeRuleVo queryByType(String ruleType) {
        CodeRule entity = codeRuleMapper.selectOne(
            Wrappers.<CodeRule>lambdaQuery().eq(CodeRule::getRuleType, ruleType));
        return entity == null ? null : MapstructUtils.convert(entity, CodeRuleVo.class);
    }

    public CodeRuleVo queryById(Long id) {
        CodeRule entity = codeRuleMapper.selectById(id);
        return entity == null ? null : MapstructUtils.convert(entity, CodeRuleVo.class);
    }

    @Transactional
    public void updateByBo(CodeRuleBo bo) {
        CodeRule update = MapstructUtils.convert(bo, CodeRule.class);
        codeRuleMapper.updateById(update);
    }

    // ==================== 通用编码生成（单据号、箱码、货架等） ====================

    /**
     * 根据编码类型生成编码。
     * 若规则不存在或未启用，返回 null（调用方自行降级处理）。
     */
    @Transactional
    public String generateCode(String ruleType) {
        CodeRule rule = codeRuleMapper.selectOne(
            Wrappers.<CodeRule>lambdaQuery().eq(CodeRule::getRuleType, ruleType));

        if (rule == null || !"0".equals(rule.getEnabled())) {
            return null;
        }

        codeRuleMapper.incrementSeq(rule.getId(), 1);
        rule = codeRuleMapper.selectById(rule.getId());
        long seq = rule.getCurrentSeq();

        return buildCode(rule, seq);
    }

    /**
     * 批量生成编码（一次原子递增 N 步，内存中构建 N 个编码）。
     * 若规则不存在或未启用，返回空列表（调用方降级处理）。
     */
    @Transactional
    public List<String> generateBatchCodes(int count, String ruleType) {
        if (count <= 0) {
            return new ArrayList<>();
        }
        CodeRule rule = codeRuleMapper.selectOne(
            Wrappers.<CodeRule>lambdaQuery().eq(CodeRule::getRuleType, ruleType));
        if (rule == null || !"0".equals(rule.getEnabled())) {
            return new ArrayList<>();
        }

        codeRuleMapper.incrementSeq(rule.getId(), count);
        rule = codeRuleMapper.selectById(rule.getId());
        long endSeq = rule.getCurrentSeq();
        long startSeq = endSeq - count + 1;

        List<String> codes = new ArrayList<>(count);
        if ("random".equals(rule.getSeqMethod())) {
            for (int i = 0; i < count; i++) {
                codes.add(buildCode(rule, 0L));
            }
        } else {
            for (long seq = startSeq; seq <= endSeq; seq++) {
                codes.add(buildCode(rule, seq));
            }
        }
        return codes;
    }

    // ==================== 器材实例编码（四段式，按一级分类+二级分类独立编号） ====================

    /**
     * 生成器材实例编码（单个）。
     * 格式：{prefix}-{level1Code}-{level2Code}-{seq}
     * 序号按 (level1Code, level2Code) 组合独立从1递增。
     * 若规则不存在或未启用，返回 null（调用方降级处理）。
     *
     * @param level1Code 一级分类编码
     * @param level2Code 二级分类编码
     */
    @Transactional
    public String generateItemInstanceCode(String level1Code, String level2Code) {
        CodeRule rule = loadItemRule();
        if (rule == null) {
            return null;
        }

        long seq = upsertSeq(level1Code, level2Code, 1);
        return buildItemInstanceCode(rule, seq, level1Code, level2Code);
    }

    /**
     * 批量生成器材实例编码。
     * 一次原子递增 N 步，内存中构建 N 个编码。
     * 若规则不存在或未启用，返回空列表（调用方降级处理）。
     *
     * @param count      生成数量
     * @param level1Code 一级分类编码
     * @param level2Code 二级分类编码
     */
    @Transactional
    public List<String> generateBatchItemInstanceCodes(int count, String level1Code, String level2Code) {
        if (count <= 0) {
            return new ArrayList<>();
        }
        CodeRule rule = loadItemRule();
        if (rule == null) {
            return new ArrayList<>();
        }

        long endSeq = upsertSeq(level1Code, level2Code, count);
        long startSeq = endSeq - count + 1;

        List<String> codes = new ArrayList<>(count);
        for (long seq = startSeq; seq <= endSeq; seq++) {
            codes.add(buildItemInstanceCode(rule, seq, level1Code, level2Code));
        }
        return codes;
    }

    // ==================== 内部方法 ====================

    /**
     * 按 (level1Code, level2Code) 组合原子递增序号。
     * 采用 SELECT FOR UPDATE → INSERT/UPDATE 两步法，兼容 VastBase。
     */
    private long upsertSeq(String level1Code, String level2Code, long step) {
        // 1. 查询并锁定行
        Long currentSeq = itemCodeSeqMapper.selectForUpdate(level1Code, level2Code);

        if (currentSeq == null) {
            // 2a. 行不存在，尝试插入
            try {
                itemCodeSeqMapper.insertSeq(level1Code, level2Code, step);
                return step;
            } catch (DuplicateKeyException e) {
                // 并发插入冲突，回退到更新
                itemCodeSeqMapper.incrementSeq(level1Code, level2Code, step);
                Long seq = itemCodeSeqMapper.selectForUpdate(level1Code, level2Code);
                return seq;
            }
        } else {
            // 2b. 行存在，递增
            itemCodeSeqMapper.incrementSeq(level1Code, level2Code, step);
            return currentSeq + step;
        }
    }

    private CodeRule loadItemRule() {
        CodeRule rule = codeRuleMapper.selectOne(
            Wrappers.<CodeRule>lambdaQuery().eq(CodeRule::getRuleType, "item"));
        if (rule == null || !"0".equals(rule.getEnabled())) {
            return null;
        }
        return rule;
    }

    /**
     * 拼接器材实例编码（四段式）：prefix + sep + level1Code + sep + level2Code + sep + seq
     */
    private String buildItemInstanceCode(CodeRule rule, long seq, String level1Code, String level2Code) {
        String sep = StrUtil.blankToDefault(rule.getSeparator(), "");
        StringBuilder sb = new StringBuilder();

        if (StrUtil.isNotBlank(rule.getPrefix())) {
            sb.append(rule.getPrefix()).append(sep);
        }

        String dateSuffix = formatDateSuffix(rule.getSuffixType());
        if (StrUtil.isNotBlank(dateSuffix)) {
            sb.append(dateSuffix).append(sep);
        }

        sb.append(level1Code).append(sep);
        sb.append(level2Code).append(sep);
        sb.append(StrUtil.padPre(String.valueOf(seq), rule.getSeqLength(), '0'));

        return sb.toString();
    }

    /**
     * 拼接通用编码：prefix + separator + dateSuffix + separator + seq
     */
    private String buildCode(CodeRule rule, long seq) {
        String sep = StrUtil.blankToDefault(rule.getSeparator(), "");
        StringBuilder sb = new StringBuilder();

        if (StrUtil.isNotBlank(rule.getPrefix())) {
            sb.append(rule.getPrefix()).append(sep);
        }

        String dateSuffix = formatDateSuffix(rule.getSuffixType());
        if (StrUtil.isNotBlank(dateSuffix)) {
            sb.append(dateSuffix).append(sep);
        }

        String seqStr;
        if ("random".equals(rule.getSeqMethod())) {
            seqStr = generateRandomSeq(rule.getSeqLength());
        } else {
            seqStr = String.valueOf(seq);
        }
        sb.append(StrUtil.padPre(seqStr, rule.getSeqLength(), '0'));

        return sb.toString();
    }

    private String formatDateSuffix(String suffixType) {
        if (StrUtil.isBlank(suffixType)) {
            return "";
        }
        LocalDate now = LocalDate.now();
        return switch (suffixType) {
            case "ymd" -> now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            case "ym"  -> now.format(DateTimeFormatter.ofPattern("yyyyMM"));
            case "y"   -> now.format(DateTimeFormatter.ofPattern("yyyy"));
            default    -> "";
        };
    }

    private String generateRandomSeq(int length) {
        int bound = (int) Math.pow(10, length);
        int random = ThreadLocalRandom.current().nextInt(1, bound);
        return String.valueOf(random);
    }
}
