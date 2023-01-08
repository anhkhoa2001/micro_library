package org.micro;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class LibraryServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(LibraryServiceApplication.class).web(WebApplicationType.NONE).run(args);
    }
}