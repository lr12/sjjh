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
 * 一次调用要发送的回复
 * Created by Nekonekod on 2017/5/2.
 */
@Slf4j
public class SendResponseModel {

    /**
     * 请求流水号
     */
    private String requestId;
    /**
     * 重组后的xml参数
     */
    private String xml;
    /**
     * 重组所对应的queueBank
     */
    private List<QueueBank> responses;
    /**
     * 接收结果
     */
    private Result result;


    public String getXml() {
        return xml;
    }

    public List<QueueBank> getResponses() {
        return responses;
    }

    public Result getResult() {
        return result;
    }

    public String getRequestId() {
        return requestId;
    }

    /**
     * 重组回复
     * @param list 所有待回复记录
     * @return
     */
    private static List<SendResponseModel> rebuildResponse(List<QueueBank> list){
        Map<String,SendResponseModel> map = new LinkedHashMap<>();//key:requestId
        for(QueueBank qb:list){
            String requestId = qb.getRequestId();
            //按请求流水号分组
            if(!map.containsKey(requestId)){
                SendResponseModel model = new SendResponseModel();
                model.requestId = requestId;
                model.responses = new ArrayList<>();
                map.put(requestId,model);
            }
            SendResponseModel model = map.get(requestId);
            model.responses.add(qb);
        }
        //重构xml
        for(SendResponseModel model:map.values()){
            try {
                model.xml = rebuildXml(model);
            } catch (Exception e) {
                model.xml = null;
                model.result = new Result(0,"重组回复请求时发生错误:"+e.getMessage());
                log.error("重组回复请求时发生错误:"+e.getMessage(),e);
            }
        }
        return new ArrayList<>(map.values());
    }

    private static String rebuildXml(SendResponseModel model) throws Exception {
        if(CollectionUtil.isNotEmpty(model.responses)){
            //创建一个xml文档
            Document document = DocumentHelper.createDocument();
            Element root = DocumentHelper.createElement("request");
            document.setRootElement(root);
            root.addAttribute("FY_QQLSH", model.requestId);//添加请求流水号
            for(QueueBank qb:model.responses){
                Document doc1 = DocumentHelper.parseText(qb.getDecodedResult());
                Element ele1 = doc1.getRootElement();
                ele1.addAttribute("FY_QQYHBS",qb.getReplier()); //添加请求银行标识
                root.add(ele1);
            }
            return document.asXML();
        }
        return null;
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
    public static class SendResponseCallback implements Callable<List<SendResponseModel>>{
        private String tag;
        private String address;
        private List<QueueBank> allResponses;

        public SendResponseCallback(String tag, String address, List<QueueBank> responses) {
            this.tag = tag;
            this.address = address;
            this.allResponses = responses;
        }

        @Override
        public List<SendResponseModel> call() throws Exception {
            log.info(tag);
            List<SendResponseModel> future = new ArrayList<>();
            //按接口标识分类
            Map<String,List<QueueBank>> map = SendResponseModel.tellByInterfaceId(this.allResponses);
            for(Map.Entry<String,List<QueueBank>> entry:map.entrySet()){
                String interfaceId = entry.getKey();
                List<QueueBank> responses = entry.getValue();
                //按请求流水号分组重构xml
                List<SendResponseModel> models = rebuildResponse(responses);
                for(SendResponseModel model:models){
                    if(StringUtil.isNotBlank(model.xml)){
                        try{
                            //调用ws
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
