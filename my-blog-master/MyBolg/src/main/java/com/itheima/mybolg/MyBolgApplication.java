package com.itheima.mybolg;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(basePackages = "com.itheima.mybolg.dao")
@SpringBootApplication
public class MyBolgApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBolgApplication.class, args);
    }

}
