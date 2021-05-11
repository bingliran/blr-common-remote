package com.blr19c.common.remote.config.load;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明成为一个api 被服务提供
 * 当一个类实现多个接口时带有@RpcApi的会被采用
 *
 * @author blr
 * @since 2021.5.11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcApi {
}
