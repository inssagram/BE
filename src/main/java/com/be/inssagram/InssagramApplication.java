package com.be.inssagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InssagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(InssagramApplication.class, args);
    }

}
