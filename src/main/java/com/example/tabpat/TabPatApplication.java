package com.example.tabpat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.tabpat.dao")
public class TabPatApplication {

    public static void main(String[] args) {
        SpringApplication.run(TabPatApplication.class, args);
    }

}
