package org.micro;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SearchServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SearchServiceApplication.class).web(WebApplicationType.NONE).run(args);
    }
}