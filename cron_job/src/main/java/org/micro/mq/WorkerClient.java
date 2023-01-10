package org.micro.mq;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class WorkerClient {

    @Autowired
    @Qualifier("queueWorker")
    private Queue queue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(final String email) {
        rabbitTemplate.convertAndSend(queue.getName(), email);
    }
}
