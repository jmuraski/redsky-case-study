package com.jmuraski.config

import com.jmuraski.util.ApplicationConstants
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.Executor

@Configuration
class AsyncConfiguration {

    @Bean (name = ApplicationConstants.ITEM_TASK_EXECUTOR)
    Executor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor()
        executor.setCorePoolSize(5)
        executor.setMaxPoolSize(12)
        executor.setQueueCapacity(1000)
        executor.setThreadNamePrefix("ItemAsyncPool-")
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.initialize()
        return executor
    }
}
