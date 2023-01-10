package org.micro.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WokerConfig {
    @Value("${cron_job.worker.queue}")
    private String queueName;

    @Bean
    public Queue queueWorker() {
        return new Queue(queueName);
    }

    @Bean
    public WorkerClient workerClient() {
        return new WorkerClient();
    }

    @Bean
    public WorkerServer workerServer() {
        return new WorkerServer();
    }
}
