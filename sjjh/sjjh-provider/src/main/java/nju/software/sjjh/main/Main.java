package nju.software.sjjh.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 项目启动入口
 * Created by Nekonekod on 2017/4/17.
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        try{
            Main.getContext();
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
    private Main() {
        this.context = new ClassPathXmlApplicationContext(
                "classpath:spring/spring-config.xml",
                "classpath:spring/provider.xml");
    }

    private ApplicationContext context ;

    public static ApplicationContext getContext() {
        return Holder.instance.context;
    }


    private static class Holder {
        static Main instance = new Main();
    }
}
