package com.helper.oaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/13 - 下午3:09
 * Created by IntelliJ IDEA.
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
public class OAapiApplication {

    public static void main(String[] args) {
        SpringApplication.run( OAapiApplication.class, args );
    }
}
