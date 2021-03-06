package com.helper.system; /**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/13 - 下午2:30
 * Created by IntelliJ IDEA.
 */

import com.helper.shiro.EnableShiroConfig;
import jdk.nashorn.internal.runtime.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableShiroConfig
@EnableEurekaClient
@EnableFeignClients
@EnableRedisHttpSession
public class SystemApplication {
    public static void main(String[] args) {
//        new SpringApplicationBuilder(SystemApplication.class).web(WebApplicationType.SERVLET).run(args);
        SpringApplication.run( SystemApplication.class, args );
    }
}
