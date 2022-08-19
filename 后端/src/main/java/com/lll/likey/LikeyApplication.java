package com.lll.likey;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.lll", "org.n3r.idworker"})
@MapperScan("com.lll.likey.mapper")
@ServletComponentScan(basePackages = "com.lll.likey")
@SpringBootApplication
//@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class LikeyApplication {

    @Bean
    public SpringUtil getSpringUtil(){
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(LikeyApplication.class, args);
    }

}
