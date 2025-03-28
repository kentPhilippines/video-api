package com.video.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.common.response.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;
    
    // 不需要验证token的路径
    private static final List<String> WHITELIST = Arrays.asList(
            "/auth/register",
            "/auth/login",
            "/auth/login/code",
            "/auth/login/oauth",
            "/auth/code/send",
            "/auth/token/refresh",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );
    
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // 检查是否是白名单路径
        if (isWhiteListPath(path)) {
            return chain.filter(exchange);
        }

        // 获取token
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(token)) {
            return unauthorized(exchange, "未登录");
        }

        // 去掉Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 调用认证服务验证token
        String finalToken = token;
        return webClientBuilder.build()
                .get()
                .uri("lb://video-auth/auth/token/validate?accessToken=" + finalToken)
                .retrieve()
                .bodyToMono(R.class)
                .flatMap(result -> {
                    if (result.getCode() != 0) {
                        return unauthorized(exchange, "token无效或已过期");
                    }

                    // 将用户ID添加到请求头
                    ServerHttpRequest newRequest = request.mutate()
                            .header("X-User-Id", String.valueOf(result.getData()))
                            .build();
                    
                    return chain.filter(exchange.mutate().request(newRequest).build());
                })
                .onErrorResume(e -> unauthorized(exchange, "token验证失败"));
    }

    @Override
    public int getOrder() {
        return -100; // 确保在其他过滤器之前执行
    }

    private boolean isWhiteListPath(String path) {
        return WHITELIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        R<?> result = R.error(401, message);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(result);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
} 