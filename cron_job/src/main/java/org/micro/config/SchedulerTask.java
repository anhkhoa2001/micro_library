package org.micro.config;

import lombok.extern.slf4j.Slf4j;
import org.micro.dto.BookDTO;
import org.micro.dto.BorrowBookDTO;
import org.micro.dto.UserDTO;
import org.micro.mq.WorkerClient;
import org.micro.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SchedulerTask {
    public static final String TASK_SPLIT = "!!!@@@@";
    @Autowired
    private JobService jobService;

    @Autowired
    private WorkerClient workerClient;

    @Scheduled(fixedRate = 60000)
    public void doit() {
        List<BorrowBookDTO> dtos = jobService.getAllBorrow();
        for(BorrowBookDTO d:dtos) {
            UserDTO userDTO = jobService.getUserById(d.getUser_id());
            BookDTO bookDTO = jobService.getBookById(d.getBook_id());

            workerClient.send(userDTO.getUsername() + TASK_SPLIT + bookDTO.getName() + TASK_SPLIT + d.getDue_date());
        }

        log.info("Hoàn tất đẩy các email lên work queue");
    }
}
