package com.video.common.cache;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的缓存key生成器实现
 */
@Component
public class DefaultCacheKeyGenerator implements CacheKeyGenerator {
    
    @Override
    public String generateCacheName(Class<?> targetClass, Method method, Object[] params) {
        // 默认使用 类名:方法名 作为缓存名称
        String className = targetClass.getSimpleName().toLowerCase();
        String methodName = method.getName();
        
        // 对特定的方法前缀进行处理
        if (methodName.startsWith("get")) {
            methodName = methodName.substring(3);
        } else if (methodName.startsWith("find")) {
            methodName = methodName.substring(4);
        } else if (methodName.startsWith("query")) {
            methodName = methodName.substring(5);
        }
        
        methodName = methodName.toLowerCase();
        return className + ":" + methodName;
    }
    
    @Override
    public String generateKey(Class<?> targetClass, Method method, Object[] params) {
        List<String> keyParts = new ArrayList<>();
        
        // 获取方法参数名
        Parameter[] parameters = method.getParameters();
        
        // 组合参数值作为key
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object paramValue = params[i];
            
            // 跳过不需要作为key的参数（可以通过注解配置）
            if (shouldSkipParameter(parameter)) {
                continue;
            }
            
            // 处理参数值
            String paramValueStr = processParamValue(paramValue);
            if (StringUtils.hasText(paramValueStr)) {
                keyParts.add(parameter.getName() + ":" + paramValueStr);
            }
        }
        
        return String.join(":", keyParts);
    }
    
    /**
     * 判断是否需要跳过某个参数
     */
    private boolean shouldSkipParameter(Parameter parameter) {
        // 这里可以添加自定义的跳过规则
        // 例如：通过注解标记不需要作为key的参数
        return false;
    }
    
    /**
     * 处理参数值
     */
    private String processParamValue(Object paramValue) {
        if (paramValue == null) {
            return "null";
        }
        
        // 处理集合类型
        if (paramValue instanceof List) {
            return "list:" + ((List<?>) paramValue).size();
        }
        
        // 处理数组类型
        if (paramValue.getClass().isArray()) {
            Object[] array = (Object[]) paramValue;
            return "array:" + array.length;
        }
        
        return paramValue.toString();
    }
} 