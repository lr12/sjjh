package nju.software.sjjh.aop;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

import nju.software.sjjh.util.WebServiceUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * aop拦截
 * @author lr12
 *
 */
@Slf4j
@Aspect
@Component
public class IexportdInterceptor {

	 @Pointcut("execution(* nju.software.sjjh.webservice.IexportdtsServiceImpl.sayHello(..))")  
	 private void anyMethod(){}//定义一个切入点 
	
	 @Around("anyMethod()")  
	 public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable{  
	        System.out.println("进入环绕通知");
	        String methodName = pjp.getSignature().getName();
	        Object[] args=pjp.getArgs();
	        Object object = pjp.proceed();//执行该方法  
	        System.out.println("退出方法");  
	        //调用转发的webservice
	        try{
	        object=WebServiceUtil.invode("http://localhost:8892/nju.software.sjjh.webservice.IexportdtsService?wsdl","getCaseVod" , "xml", (String)args[0]);
	        }
	        catch (Exception e) {
				e.printStackTrace();
			}
	        return object; 
	       
	 }
}
