package nju.software.sjjh.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 中转服务
 * @author lr12
 *
 */
@Slf4j
public class Zzfw {
    public static void main(String[] args) {
        try{
        	Zzfw.getContext();
            log.debug("sjjh-provider start...");
            System.out.println("sjjh service server started!");
            System.in.read();
        }catch (Throwable t){
            log.error("sjjh service server failed!",t);
        }
    }


    /**
     * singleton
     */
    private Zzfw() {
        this.context = new ClassPathXmlApplicationContext(
                "classpath:spring/provider-zzfw.xml");
    }

    private ApplicationContext context ;

    public static ApplicationContext getContext() {
        return Holder.instance.context;
    }


    private static class Holder {
        static Zzfw instance = new Zzfw();
    }
}
