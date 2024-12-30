package com.kindred.mir.util.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MirAspect {

  //@AfterReturning(pointcut = "@annotation(com.kindred.mir.util.aspect.MirSetWithAutoRefresh) && execution(* *(..))", returning = "result")
  @After("@annotation(com.kindred.mir.util.aspect.MirSetWithAutoRefresh) && execution(* *(..))")
  public void afterAdvice(JoinPoint joinPoint) {
    System.out.println("After Method " + joinPoint.getSignature().getName());
  }

}
