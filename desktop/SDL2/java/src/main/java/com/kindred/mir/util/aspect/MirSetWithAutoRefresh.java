package com.kindred.mir.util.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AspectJ看起来不能注入lombok生成的setter方法,
 * 所以我们还是选择需要refresh texture的setter方法自己定义并且加上此注解的方式
 * 实现注入自动刷新texture
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MirSetWithAutoRefresh
{
}
