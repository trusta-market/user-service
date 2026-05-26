package com.trusta_market.userservice.common.config;

import com.trustamarket.common.response.CommonResponseAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

// CommonAutoConfiguration의 @ConditionalOnMissingBean 조건에 의해
// 이 빈이 먼저 등록되면 자동 설정 빈은 생성되지 않음
@Configuration
public class CustomResponseAdviceConfig {

    @Bean
    public CommonResponseAdvice commonResponseAdvice() {
        return new CommonResponseAdvice() {
            @Override
            public Object beforeBodyWrite(Object body,
                    MethodParameter returnType,
                    MediaType selectedContentType,
                    Class<? extends HttpMessageConverter<?>> selectedConverterType,
                    ServerHttpRequest request,
                    ServerHttpResponse response) {

                // Internal API는 Feign Client가 raw DTO를 기대하므로 wrapping 제외
                if (request.getURI().getPath().startsWith("/internal")) {
                    return body;
                }

                return super.beforeBodyWrite(body, returnType, selectedContentType,
                        selectedConverterType, request, response);
            }
        };
    }
}
