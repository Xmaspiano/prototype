package com.helper.eureka;
/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/13 - 下午2:30
 * Created by IntelliJ IDEA.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run( EurekaApplication.class, args );
    }

}
