package com.helper.application.APIservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/29 - 上午9:28
 * Created by IntelliJ IDEA.
 */
@Component
@FeignClient(value = "system") //这里的name对应调用服务的spring.applicatoin.name
public interface ServiceAFeignClient {
    @RequestMapping(value = "/test/hi")
    String hi(@RequestParam("id") String id);
}
