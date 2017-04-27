package nju.software.sjjh.bank.model;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.util.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

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
     * 回复流水号
     */
    private String responseId;
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


    public String getXml() {
        return xml;
    }

    public List<QueueBank> getRequests() {
        return requests;
    }

    public Result getResult() {
        return result;
    }

    public String getResponseId() {
        return responseId;
    }

    /**
     * 重组最大数目
     */
    private static final int QUERY_MAXSIZE = 10;

    /**
     * 重组请求
     * @param list 所有待发送的查询
     * @param maxSize 一次调用的最大查询数
     * @return
     */
    private static List<SendRequestModel> rebuildRequest(List<QueueBank> list, int maxSize){
        int modelSize = (int) Math.ceil(list.size()/(double)maxSize);
        List<SendRequestModel> results = new ArrayList<>(modelSize);
        for (int i=0;i<modelSize;i++){
            SendRequestModel model = new SendRequestModel();
            int fromIndex = i * maxSize;
            int toIndex = Math.min(fromIndex + maxSize,list.size());
            List<QueueBank> subList = list.subList(fromIndex, toIndex);
            model.requests = new ArrayList<>(subList);
            try {
                model.xml = rebuildXml(model);
            } catch (Exception e) {
                model.xml = null;
                model.result = new Result(0,"重组银行查询请求时发生错误:"+e.getMessage());
                log.error("重组银行查询请求时发生错误:"+e.getMessage(),e);
            }
            results.add(model);
        }
        return results;
    }

    private static String rebuildXml(SendRequestModel model) throws Exception {
        if(CollectionUtil.isNotEmpty(model.requests)){
            //创建一个xml文档
            Document document = DocumentHelper.createDocument();
            Element root = DocumentHelper.createElement("request");
            document.setRootElement(root);
            //TODO 设置 法院_任务流水号
            model.responseId = UuidUtil.generateUuid();
            root.addAttribute("FY_RWLSH", model.responseId);
            for(QueueBank qb:model.requests){
                Document doc1 = DocumentHelper.parseText(qb.getDecodedParam());
                Element ele1 = doc1.getRootElement();
                root.add(ele1);
            }
            return document.asXML();
        }
        return null ;
    }

    /**
     * 按请求接口标识分组
     * @param all
     * @return
     */
    private static Map<String,List<QueueBank>> tellByInterfaceId(List<QueueBank> all){
        Map<String,List<QueueBank>> map = new LinkedHashMap<>();
        for(QueueBank req:all){
            String interfaceId = req.getResponseInterfaceId();
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
                    if(StringUtil.isNotBlank(model.xml)){
                        try{
                            //调用银行ws
//                            String resultXml = WebServiceUtil.invode(this.address, interfaceId, "params", model.xml);
                            String resultXml = CxfUtil.jump(this.address, interfaceId, model.xml)[0].toString();
                            //set 返回结果
                            model.result = XmlUtil.toBean(resultXml, Result.class);
                        }catch (Exception e){
                            //捕获远程调用时发生的异常
                            model.result = new Result(0,e.getMessage());
                        }
                    }
                    future.add(model);
                    //休眠50ms
                    Thread.sleep(50);
                }
            }
            return future;
        }
    }
}
