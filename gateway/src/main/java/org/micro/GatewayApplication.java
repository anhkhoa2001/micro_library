package org.micro;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(GatewayApplication.class, args);
    }

    //gateway 8090
    //user 8091
    //library 8092
    //search 8093
    //bookloan 8094
    //report 8095
    //cronjob 8096
}