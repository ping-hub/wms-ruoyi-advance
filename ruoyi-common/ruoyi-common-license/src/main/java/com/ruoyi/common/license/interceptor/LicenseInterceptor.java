package com.ruoyi.common.license.interceptor;

import com.ruoyi.common.license.LicenseConstants;
import com.ruoyi.common.license.service.LicenseStatusHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * License 拦截器
 * - GET 请求始终放行（只读模式）
 * - POST/PUT/DELETE 请求在 License 无效时拦截
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LicenseInterceptor implements HandlerInterceptor {

    private final LicenseStatusHolder statusHolder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // GET 请求始终放行（允许查看）
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method) || "OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // License 有效则放行
        if (statusHolder.isValid()) {
            return true;
        }

        // License 无效，拦截写操作
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> result = new HashMap<>();
        result.put("code", LicenseConstants.CODE_LICENSE_INVALID);
        result.put("msg", LicenseConstants.MSG_LICENSE_INVALID);

        response.getWriter().write(objectMapper.writeValueAsString(result));
        return false;
    }
}
