package nju.software.sjjh.bank.model;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.util.CollectionUtil;
import nju.software.sjjh.util.WebServiceUtil;
import nju.software.sjjh.util.XmlUtil;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 一次调用要发送的请求
 * Created by Nekonekod on 2017/4/19.
 */
@Slf4j
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
     * 接收结果
     */
    private Result result;


    /**
     * 重组最大数目
     */
    private static final int QUERY_MAXSIZE = 10;


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

    /**
     * 按接口标识分组
     * @param all
     * @return
     */
    private static Map<String,List<QueueBank>> tellByInterfaceId(List<QueueBank> all){
        Map<String,List<QueueBank>> map = new LinkedHashMap<>();
        for(QueueBank req:all){
            String interfaceId = req.getInterfaceId();
            if(!map.containsKey(interfaceId)) map.put(interfaceId,new ArrayList<QueueBank>());
            map.get(interfaceId).add(req);
        }
        return map;
    }

    /**
     * 发送请求线程
     */
    public static class SendRequestCallback implements Callable<List<SendRequestModel>>{
        private String tag;
        private String address;
        private List<QueueBank> allRequsts;

        public SendRequestCallback(String tag, String address, List<QueueBank> requsts) {
            this.tag = tag;
            this.address = address;
            this.allRequsts = requsts;
        }

        @Override
        public List<SendRequestModel> call() throws Exception {
            log.info(tag);
            List<SendRequestModel> future = new ArrayList<>();
            //按接口标识分类
            Map<String,List<QueueBank>> map = SendRequestModel.tellByInterfaceId(this.allRequsts);
            for(Map.Entry<String,List<QueueBank>> entry:map.entrySet()){
                String interfaceId = entry.getKey();
                List<QueueBank> requests = entry.getValue();
                //按最大数目分组
                List<SendRequestModel> models = rebuildRequest(requests, QUERY_MAXSIZE);
                for(SendRequestModel model:models){
                    //调用银行ws
                    String resultXml = WebServiceUtil.invode(this.address, interfaceId, "params", model.xml);
                    //set 返回结果
                    model.result = XmlUtil.toBean(resultXml, Result.class);
                    future.add(model);
                }
            }
            return future;
        }
    }

    public String getXml() {
        return xml;
    }

    public List<QueueBank> getRequests() {
        return requests;
    }

    public Result getResult() {
        return result;
    }

}
