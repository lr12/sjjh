package nju.software.sjjh.bank.model;

import lombok.Data;
import nju.software.sjjh.bank.entity.QueueBank;

import java.util.*;

/**
 * 要发送给银行的请求
 * Created by Nekonekod on 2017/4/19.
 */
@Data
public class SendRequestBankModel {

    private String bankId;
    /**
     * key:接口标识符，value：请求列表
     */
    private Map<String,List<QueueBank>> map ;

    public SendRequestBankModel(String bankId) {
        this.bankId = bankId;
        this.map = new LinkedHashMap<>();
    }

    /**
     * 将list返回为SendRequestModel
     * @param list
     * @return
     */
    public static List<SendRequestBankModel> buildModelsFromList(List<QueueBank> list){
        Map<String,SendRequestBankModel> bankMap = new HashMap<>();
        for(QueueBank b:list){
            String bankId = b.getReplier();//银行标识
            //如果map没有该银行，则添加
            if(!bankId.contains(bankId)) bankMap.put(bankId,new SendRequestBankModel(bankId));
            SendRequestBankModel bank = bankMap.get(bankId);
            if(bank!=null){
                //将请求按接口标识分类
                Map<String, List<QueueBank>> requestMap = bank.getMap();
                String interfaceId = b.getInterfaceId();
                if(!requestMap.containsKey(interfaceId)) requestMap.put(interfaceId,new ArrayList<QueueBank>());
                requestMap.get(interfaceId).add(b);
            }
        }
        return new ArrayList<>(bankMap.values());
    }

}
