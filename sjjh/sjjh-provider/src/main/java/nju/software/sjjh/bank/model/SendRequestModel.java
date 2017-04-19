package nju.software.sjjh.bank.model;

import nju.software.sjjh.bank.entity.QueueBank;

import java.util.ArrayList;
import java.util.List;

/**
 * 要发送的请求
 * Created by Nekonekod on 2017/4/19.
 */
public class SendRequestModel {

    /**
     * 重组后的xml参数
     */
    private String xml;
    /**
     * 重组所对应的queueBank
     */
    private List<QueueBank> requests;

    /**
     * 重组请求
     * @param list
     * @param maxSize 一个请求的最大查询数
     * @return
     */
    public static List<SendRequestModel> rebuildRequest(List<QueueBank> list,int maxSize){
        int modelSize = (int) Math.ceil(list.size()/(double)maxSize);
        List<SendRequestModel> results = new ArrayList<>(modelSize);
        for (int i=0;i<modelSize;i++){
            SendRequestModel model = new SendRequestModel();
            int fromIndex = i * maxSize;
            int toIndex = Math.min(fromIndex + maxSize,list.size()-1);
            List<QueueBank> subList = list.subList(fromIndex, toIndex);
            model.requests = new ArrayList<>(subList);
            model.xml = rebuildXml(subList);
            results.add(model);
        }
        return results;
    }

    private static String rebuildXml(List<QueueBank> subList) {

        return "" ;
    }

}
