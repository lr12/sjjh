package nju.software.sjjh.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取属性文件工具
 * Created by Nekonekod on 2017/4/18.
 */
@Slf4j
public class PropertiesUtil {

    public static String getProp(String path,String name) {
        ClassPathResource resource = new ClassPathResource(path);
        Properties pros = new Properties();
        try {
            pros.load(resource.getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return pros.getProperty(name);
    }

}
