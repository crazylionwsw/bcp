package com.fuze.bcp.dubbo.annotation;

import java.lang.annotation.*;

/**
 * Drools规则引擎注解
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface DroolsConfiguration {

}
