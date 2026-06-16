package com.ruoyi.wms.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.wms.domain.bo.CodeRuleBo;
import com.ruoyi.wms.domain.entity.CodeRule;
import com.ruoyi.wms.domain.vo.CodeRuleVo;
import com.ruoyi.wms.mapper.CodeRuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 编码规则服务
 */
@Service
@RequiredArgsConstructor
public class CodeRuleService {

    private final CodeRuleMapper codeRuleMapper;

    // ==================== CRUD ====================

    public List<CodeRuleVo> queryList() {
        List<CodeRule> list = codeRuleMapper.selectList(
            Wrappers.<CodeRule>lambdaQuery().orderByAsc(CodeRule::getRuleType));
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

    // ==================== 核心编码生成 ====================

    /**
     * 根据编码类型生成编码。
     * 若规则不存在或未启用，返回 null（调用方自行降级处理）。
     */
    @Transactional
    public String generateCode(String ruleType) {
        CodeRule rule = codeRuleMapper.selectOne(
            Wrappers.<CodeRule>lambdaQuery().eq(CodeRule::getRuleType, ruleType));

        if (rule == null || !"0".equals(rule.getEnabled())) {
            return null; // 规则不存在或未启用，调用方降级
        }

        // 1. 递增序号
        codeRuleMapper.incrementSeq(rule.getId(), 1);
        // 重新读取最新值
        rule = codeRuleMapper.selectById(rule.getId());
        long seq = rule.getCurrentSeq();

        // 2. 拼接编码
        return buildCode(rule, seq);
    }

    /**
     * 拼接编码：prefix + separator + dateSuffix + separator + seq
     */
    private String buildCode(CodeRule rule, long seq) {
        String sep = StrUtil.blankToDefault(rule.getSeparator(), "");
        StringBuilder sb = new StringBuilder();

        // 前缀
        if (StrUtil.isNotBlank(rule.getPrefix())) {
            sb.append(rule.getPrefix()).append(sep);
        }

        // 日期后缀
        String dateSuffix = formatDateSuffix(rule.getSuffixType());
        if (StrUtil.isNotBlank(dateSuffix)) {
            sb.append(dateSuffix).append(sep);
        }

        // 序号
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
