package com.blr19c.common.remote.config.load;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 成为服务提供者/同时也会成为一个springboot组件
 *
 * @author blr
 * @since 2021.4.16
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    /**
     * 服务名称
     */
    @AliasFor(annotation = Component.class)
    String value() default "";
}
