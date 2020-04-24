package com.cashbang.annotations;

import java.lang.annotation.*;

/**
 * @Author: huangdj
 * @Date: 2020/4/24
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DJRequestParam {
    String value() default "";
}
