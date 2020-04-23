package com.cashbang.annotations;

import java.lang.annotation.*;

/**
 * @Author: huangdj
 * @Date: 2020/4/23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DJController {

    String value() default "";

}
