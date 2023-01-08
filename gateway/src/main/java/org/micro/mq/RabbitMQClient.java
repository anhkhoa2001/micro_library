package org.micro.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQClient {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public String callRpcService(String exchangeName, String queueName, String key, String msg) {
        try {
            log.info("callRpcService - exchangeName: {}, queueName: {}, key : {}, msg: {}",
                    exchangeName, queueName, key, msg);
            //Send msg
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            Object obj = amqpTemplate.convertSendAndReceive(exchangeName, key, message);
            return (String) obj;
        } catch (Exception ex) {
            log.error("callRpcService Exception >>> " + ex.toString());
            ex.printStackTrace();
        }
        return null;
    }

    public boolean callPublishService(String exchangeName, String key, String msg) {
        log.info("callPublishService - exchangeName: {}, key : {}", exchangeName, key);

        try {
            //Send msg
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            amqpTemplate.convertAndSend(exchangeName, key, message);
        } catch (Exception ex) {
            log.error("callPublishService Exception >>> " + ex.toString());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean callWorkerService(String queueName, String msg) {
        log.info("callWorkerService - queueName : {}", queueName);

        try {
            //Send msg
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            amqpTemplate.convertAndSend(queueName, message);
        } catch (Exception ex) {
            log.error("callWorkerService Exception >>> " + ex.toString());
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
