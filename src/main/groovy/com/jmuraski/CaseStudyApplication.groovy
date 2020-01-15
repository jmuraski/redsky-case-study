package com.jmuraski

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@EnableRedisRepositories
class CaseStudyApplication {

    static void main(String[] args) {
        SpringApplication.run(CaseStudyApplication.class)
    }
}
