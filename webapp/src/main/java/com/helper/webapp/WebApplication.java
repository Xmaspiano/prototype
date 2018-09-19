package com.helper.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/15 - 上午10:43
 * Created by IntelliJ IDEA.
 */
@SpringBootApplication
@EnableEurekaClient
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run( WebApplication.class, args );
    }

}
