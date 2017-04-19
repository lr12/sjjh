package nju.software.sjjh.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by Nekonekod on 2017/4/18.
 */
@Service
public class HelloService {


    @Cacheable("TEST_CACHE")
    public String getStr(String orig){
        System.out.println("in getStr");
        return "getStr:" + orig;
    }

}
