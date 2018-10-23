package com.helper.shiro;

import com.helper.shiro.config.HelperShiroConfiguration;
import com.helper.shiro.config.HttpSessionConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/27 - 下午4:06
 * Created by IntelliJ IDEA.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({HelperShiroConfiguration.class})
public @interface EnableShiroConfig {

}