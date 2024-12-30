package com.kindred.mir.test;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class TestAspectJ {

//  // 在任何 public 方法调用前打印日志信息
//  @Before("execution(public * com.kindred.mir.test.TestLombok.test(..))")
//  public void logBefore(JoinPoint joinPoint) {
//    System.out.println("Before method: " + joinPoint.getSignature().getName());
//  }


  // 在成功返回后打印日志信息
//  @AfterReturning(pointcut = "execution(public * com.kindred.mir.test.TestLombok.Bean.test(..))", returning = "result")
//  public void logAfterReturning(JoinPoint joinPoint, Object result) {
//    System.out.println("Method " + joinPoint.getSignature().getName() + " returned with value " + result);
//  }

//  @AfterReturning(pointcut = "@annotation(com.kindred.mir.util.aspect.MirSetWithAutoRefresh) && execution(* *(..))", returning = "result")
//  public void logAfterReturning(JoinPoint joinPoint, Object result) {
//    System.out.println("Method " + joinPoint.getSignature().getName() + " returned with value " + result);
//  }


//  // 捕获异常并打印错误信息
//  @AfterThrowing(pointcut = "execution(public * com.kindred.mir.test.TestLombok.test(..))", throwing = "error")
//  public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
//    System.out.println("Method " + joinPoint.getSignature().getName() + " threw exception: " + error);
//  }

//  @AfterThrowing(pointcut = "@annotation(com.kindred.mir.util.aspect.MirSetWithAutoRefresh) && execution(* *(..))", throwing = "error")
//  public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
//    System.out.println("Method " + joinPoint.getSignature().getName() + " threw exception: " + error);
//  }

  // 环绕通知，可以控制是否继续执行目标方法
//  @Around("execution(public * com.kindred.mir.test.TestLombok.Bean.set*(..))")
//  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//    System.out.println("Around before method: " + joinPoint.getSignature().getName());
//    try {
//      Object result = joinPoint.proceed(); // 执行目标方法
//      System.out.println("Around after method: " + joinPoint.getSignature().getName());
//      return result;
//    } catch (Throwable e) {
//      System.out.println("Around advice threw exception: " + e);
//      throw e;
//    }
//  }

//  @Around("@annotation(com.kindred.mir.util.MirSetWithAutoRefresh) && execution(* *(..))")
//  public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable
//  {
//    Object returnObject = null;
//    System.out.println("Aspect Before");
//    try
//    {
//      returnObject = joinPoint.proceed();
//    }
//    catch (Exception ex)
//    {
//      System.out.println("Aspect Catch: " + ex.getMessage());
//    }
//    finally
//    {
//      System.out.println("Aspect After");
//    }
//    return returnObject;
//  }

  public static void main(String args[]) throws Exception{
    TestLombok.Bean bean = new TestLombok.Bean.BeanBuilder().id(1L).build();
    System.out.println(bean);
    bean.setTest("test");
    bean.setTest2("test2");
  }

}
