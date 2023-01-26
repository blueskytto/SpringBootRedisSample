package com.example.app2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@SpringBootApplication
public class App2Application {

    public static void main(String[] args) {
        SpringApplication.run(App2Application.class, args);
    }

}
