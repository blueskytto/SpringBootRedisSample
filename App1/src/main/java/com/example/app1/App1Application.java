package com.example.app1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@SpringBootApplication
public class App1Application {

    public static void main(String[] args) {
        SpringApplication.run(App1Application.class, args);
    }

}
