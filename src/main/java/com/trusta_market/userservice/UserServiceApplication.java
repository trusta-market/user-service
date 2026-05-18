package com.trusta_market.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAsync
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
}
