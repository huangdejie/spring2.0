package com.cashbang.annotations;

import java.lang.annotation.*;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DJRequestMapping {

    String value() default "";

}
