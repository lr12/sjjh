package nju.software.sjjh.bank.facade;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * 银行服务
 * Created by Nekonekod on 2017/4/17.
 */
public interface BankWebService {

    /**
     * 2.5 接收查询余额请求
     * @param params
     * @return
     */
    String requestAsyncZhye(String params);

}
