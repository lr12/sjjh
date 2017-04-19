package nju.software.sjjh.bank.facade;

/**
 * 法院对银行的服务
 * Created by Nekonekod on 2017/4/18.
 */
public interface CourtToBankWebService {

    /**
     * 2.6异步账户余额查询反馈
     * @param params
     * @return
     */
    String responseAsyncZhye(String params);

}
