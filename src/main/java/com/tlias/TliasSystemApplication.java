package com.tlias;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tlias.mapper")
public class TliasSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TliasSystemApplication.class, args);
    }

}
