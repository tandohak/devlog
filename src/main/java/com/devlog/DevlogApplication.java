package com.devlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class DevlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevlogApplication.class, args);
    }

}

