package com.yeonfish.multitool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class MultitoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultitoolApplication.class, args);
    }

}
