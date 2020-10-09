package com.clubtaekwondo.club.utils;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.ValueConstants;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestIndirectParam
{

    String value() default "";

    boolean required() default true;

    String defaultValue() default ValueConstants.DEFAULT_NONE;

}