package com.video.common.utils;

import com.video.common.exception.BusinessException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class UserContext {

    private static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException("获取用户信息失败");
        }
        jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
        String userId = request.getHeader(USER_ID_HEADER);
        if (userId == null) {
            throw new BusinessException("用户未登录");
        }
        return Long.parseLong(userId);
    }
} 