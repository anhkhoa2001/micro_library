package org.micro.mq;

import lombok.extern.slf4j.Slf4j;
import org.micro.config.SchedulerTask;
import org.micro.config.SenderEmailService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@RabbitListener(queues = "${cron_job.worker.queue}")
@Slf4j
public class WorkerServer {
    @Autowired
    private SenderEmailService senderEmailService;
    @RabbitHandler
    public void receive(String message) {
        log.info("Đã nhân được message vào lúc " + new Date() + " có giá trị: " + message);
        String[] messages = message.split(SchedulerTask.TASK_SPLIT);
        try {
            String email = messages[0];
            String name_book = messages[1];
            String due_date = messages[2];
            String body = "Bạn đã quá hạn trả sách " + name_book + " vào ngày " + due_date;
            String subject = "Thông báo quá hạn trả sách";
            senderEmailService.sendSimpleEmail(body, subject, email);
            log.info("Gửi mail thành công vào " + email);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("Thông tin email không hợp lệ");
        }

    }
}