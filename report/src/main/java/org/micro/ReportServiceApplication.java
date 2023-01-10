package org.micro;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ReportServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ReportServiceApplication.class).web(WebApplicationType.NONE).run(args);

    }
}