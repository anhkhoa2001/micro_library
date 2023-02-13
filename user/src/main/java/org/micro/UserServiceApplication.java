package org.micro;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@Slf4j
public class UserServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(UserServiceApplication.class).web(WebApplicationType.NONE).run(args);
    }
}