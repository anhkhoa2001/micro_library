package org.micro.config;

import lombok.extern.slf4j.Slf4j;
import org.micro.dto.BookDTO;
import org.micro.dto.BorrowBookDTO;
import org.micro.dto.UserDTO;
import org.micro.mq.RabbitMQClient;
import org.micro.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class SchedulerTask {
    public static final String TASK_SPLIT = "!!!@@@@";

    @Value("${cron_job.worker.queue}")
    private String queueName;
    @Autowired
    private JobService jobService;
    @Autowired
    private RabbitMQClient client;

    //chạy lúc 0 giờ mỗi ngày để kiểm trả log quá hạn để gửi mail
    @Scheduled(cron = "0 0 0 * * *")
    public void doit() {
        Set<BorrowBookDTO> dtos = new HashSet<>(jobService.getAllBorrow());
        for(BorrowBookDTO d:dtos) {
            UserDTO userDTO = jobService.getUserById(d.getUser_id());
            BookDTO bookDTO = jobService.getBookById(d.getBook_id());
            String message = userDTO.getUsername() + TASK_SPLIT + bookDTO.getName() + TASK_SPLIT + d.getDue_date();

            client.callWorkerService(queueName, message);
        }

        log.info("Hoàn tất đẩy các email lên work queue");
    }
}
