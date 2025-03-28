package com.video.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类 - 自定义实现
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private static final String HEADER_TYPE = "JWT";
    private static final String HEADER_ALG = "HS256";
    private static final String CLAIM_EXP = "exp";
    private static final String CLAIM_IAT = "iat";
    private static final String CLAIM_SUB = "sub";

    /**
     * 生成token
     *
     * @param username 用户名
     * @return token
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + this.expiration * 1000);

        // 添加标准声明
        claims.put(CLAIM_SUB, username);
        claims.put(CLAIM_IAT, now.getTime() / 1000);
        claims.put(CLAIM_EXP, expiration.getTime() / 1000);

        return createToken(claims);
    }

    /**
     * 创建token
     *
     * @param claims 声明
     * @return token
     */
    private String createToken(Map<String, Object> claims) {
        try {
            // 1. 创建header
            Map<String, Object> header = new HashMap<>();
            header.put("typ", HEADER_TYPE);
            header.put("alg", HEADER_ALG);

            // 2. Base64编码header和payload
            String headerJson = JsonUtils.toJson(header);
            String payloadJson = JsonUtils.toJson(claims);
            String headerBase64 = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
            String payloadBase64 = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

            // 3. 计算签名
            String dataToSign = headerBase64 + "." + payloadBase64;
            String signature = sign(dataToSign);

            // 4. 拼接token
            return dataToSign + "." + signature;
        } catch (Exception e) {
            log.error("生成token失败", e);
            throw new RuntimeException("生成token失败", e);
        }
    }

    /**
     * 验证token
     *
     * @param token token
     * @return 用户名
     */
    public String validateToken(String token) {
        try {
            // 1. 分割token
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Invalid token format");
            }

            // 2. 验证签名
            String dataToSign = parts[0] + "." + parts[1];
            String signature = sign(dataToSign);
            if (!signature.equals(parts[2])) {
                throw new RuntimeException("Invalid signature");
            }

            // 3. 解析payload
            String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> claims = JsonUtils.fromJson(payloadJson, Map.class);

            // 4. 验证过期时间
            Long exp = ((Number) claims.get(CLAIM_EXP)).longValue();
            if (exp * 1000 < System.currentTimeMillis()) {
                throw new RuntimeException("Token has expired");
            }

            // 5. 返回用户名
            return (String) claims.get(CLAIM_SUB);
        } catch (Exception e) {
            log.error("验证token失败", e);
            throw new RuntimeException("验证token失败", e);
        }
    }

    /**
     * 计算签名
     *
     * @param data 待签名数据
     * @return 签名
     */
    private String sign(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return base64UrlEncode(hash);
    }

    /**
     * Base64Url编码
     */
    private String base64UrlEncode(byte[] data) {
        String base64 = Base64.getEncoder().encodeToString(data);
        return base64.replace('+', '-')
                .replace('/', '_')
                .replaceAll("=+$", "");
    }

    /**
     * Base64Url解码
     */
    private byte[] base64UrlDecode(String data) {
        String base64 = data.replace('-', '+').replace('_', '/');
        // 添加padding
        switch (base64.length() % 4) {
            case 2:
                base64 += "==";
                break;
            case 3:
                base64 += "=";
                break;
        }
        return Base64.getDecoder().decode(base64);
    }

    /**
     * 从token中获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> claims = JsonUtils.fromJson(payloadJson, Map.class);
            Long exp = ((Number) claims.get(CLAIM_EXP)).longValue();
            return new Date(exp * 1000);
        } catch (Exception e) {
            log.error("获取token过期时间失败", e);
            throw new RuntimeException("获取token过期时间失败", e);
        }
    }

    /**
     * 判断token是否过期
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> claims = JsonUtils.fromJson(payloadJson, Map.class);
            
            // 更新过期时间
            long nowMillis = System.currentTimeMillis();
            claims.put(CLAIM_IAT, nowMillis / 1000);
            claims.put(CLAIM_EXP, (nowMillis + expiration * 1000) / 1000);
            
            return createToken(claims);
        } catch (Exception e) {
            log.error("刷新token失败", e);
            throw new RuntimeException("刷新token失败", e);
        }
    }
} 