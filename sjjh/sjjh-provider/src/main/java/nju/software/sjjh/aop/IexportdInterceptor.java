package nju.software.sjjh.aop;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

import nju.software.sjjh.util.ConstantUtil;
import nju.software.sjjh.util.CxfUtil;
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

	 @Pointcut("execution(* nju.software.sjjh.webservice.IexportdtsServiceImpl.*(..))")  
	 private void anyMethod(){}//定义一个切入点 
	
	 @Around("anyMethod()")  
	 public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable{  
	        log.info("进入环绕通知");
	        String methodName = pjp.getSignature().getName();
	        Object[] args=pjp.getArgs();
	        log.info("aop调用的方法:"+methodName);
	        Object object = pjp.proceed();//执行该方法  
	        log.info("退出方法");  
	        //调用转发的webservice
	        try{
	        object=CxfUtil.jump(ConstantUtil.kjftWsdl,methodName , (String)args[0])[0];
	        CxfUtil.notifySjjhpt(new String[]{"kjft","1234","dqc583sb7wmja6nj11wbbwm4snpcq5ov",methodName,"3","",(String)args[0],""});
	        
	        
	        }
	        catch (Exception e) {
	            CxfUtil.notifySjjhpt(new String[]{"kjft","1234","dqc583sb7wmja6nj11wbbwm4snpcq5ov",methodName,"4","调用wsdl有问题",(String)args[0],""});
	        	System.out.println("抛出异常");
				e.printStackTrace();
			}
	        return object; 
	       
	 }
}
