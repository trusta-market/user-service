package com.trusta_market.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "com.trusta_market.userservice",
        "com.trustamarket.common" })
@EnableJpaRepositories(basePackages = "com.trusta_market.userservice")
@EnableFeignClients(basePackages = "com.trusta_market.userservice")
@EntityScan(basePackages = {
        "com.trusta_market.userservice",
        "com.trustamarket.common"
})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner debugFilters(
            org.springframework.web.context.WebApplicationContext context) {
        return args -> {
            System.out.println(">>> 등록된 필터 목록:");
            context.getBeansOfType(jakarta.servlet.Filter.class)
                    .forEach((name, filter) -> System.out
                            .println("Filter Name: " + name + " (" + filter.getClass().getName() + ")"));
        };
    }
}
