package com.helper.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/17 - 下午4:19
 * Created by IntelliJ IDEA.
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class RouteApplication {

    public static void main(String[] args) {
        SpringApplication.run( RouteApplication.class, args );
    }
}
