package com.cashbang.annotations;

import java.lang.annotation.*;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DJAutowired {
    String value() default "";
}
