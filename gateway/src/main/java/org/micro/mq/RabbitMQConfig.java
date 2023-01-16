package org.micro.mq;

import lombok.extern.slf4j.Slf4j;
import org.micro.config.GatewayConfig;
import org.micro.util.StringUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean("connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setChannelCacheSize(40);
        connectionFactory.setRequestedHeartBeat(60);

        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReceiveTimeout(TimeUnit.SECONDS.toMillis(GatewayConfig.RPC_TIMEOUT));
        rabbitTemplate.setReplyTimeout(TimeUnit.SECONDS.toMillis(GatewayConfig.RPC_TIMEOUT));
        return rabbitTemplate;
    }

    @Bean("directAutoDeleteQueue")
    public Queue directAutoDeleteQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public void initAllQueue() {
        log.info("Initing all rpc, pub/sub and worker queue...");
        AmqpAdmin amqpAdmin = rabbitAdmin();
        Queue directAutoDeleteQueue = directAutoDeleteQueue();
        List<String> serviceList = GatewayConfig.SERVICE_LIST;
        if (serviceList != null && !serviceList.isEmpty()) {
            log.info("Total: {} services", serviceList.size());
            for (String service : serviceList) {
                log.info("+ Init for service: {}", service);
                //RPC
                String rpcQueue = GatewayConfig.SERVICE_MAP.get(service + ".rpc.queue");
                String rpcExchange = GatewayConfig.SERVICE_MAP.get(service + ".rpc.exchange");
                String rpcKey = GatewayConfig.SERVICE_MAP.get(service + ".rpc.key");
                if (!StringUtil.isNullOrEmpty(rpcQueue) && !StringUtil.isNullOrEmpty(rpcExchange) && !StringUtil.isNullOrEmpty(rpcKey)) {
                    Queue queue = new Queue(rpcQueue);
                    amqpAdmin.declareQueue(queue);
                    //Exchange
                    DirectExchange exchange = new DirectExchange(rpcExchange);
                    amqpAdmin.declareExchange(exchange);
                    //Binding
                    Binding binding = BindingBuilder.bind(queue).to(exchange).with(rpcKey);
                    amqpAdmin.declareBinding(binding);
                }
                //Pub/Sub
                String directExchange = GatewayConfig.SERVICE_MAP.get(service + ".direct.exchange");
                String directKey = GatewayConfig.SERVICE_MAP.get(service + ".direct.key");
                if (!StringUtil.isNullOrEmpty(directExchange) && !StringUtil.isNullOrEmpty(directKey)) {
                    //Exchange
                    DirectExchange exchange = new DirectExchange(directExchange);
                    amqpAdmin.declareExchange(exchange);
                    //Binding
                    Binding binding = BindingBuilder.bind(directAutoDeleteQueue).to(exchange).with(directKey);
                    amqpAdmin.declareBinding(binding);
                }
                //Worker
                String workerQueue = GatewayConfig.SERVICE_MAP.get(service + ".worker.queue");
                if (!StringUtil.isNullOrEmpty(workerQueue)) {
                    Queue queue = new Queue(workerQueue);
                    amqpAdmin.declareQueue(queue);
                }
            }
        }
        log.info("Initing all successful");
    }
}
