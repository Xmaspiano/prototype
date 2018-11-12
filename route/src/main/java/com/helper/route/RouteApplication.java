package com.helper.route;

import com.helper.shiro.EnableShiroConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

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
@EnableShiroConfig
@EnableRedisHttpSession
public class RouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteApplication.class, args);
//        new SpringApplicationBuilder(RouteApplication.class).web(WebApplicationType.SERVLET).run(args);
    }
}
