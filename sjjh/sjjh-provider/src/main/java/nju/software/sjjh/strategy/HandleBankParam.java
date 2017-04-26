package nju.software.sjjh.strategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.bank.service.BankService;
import nju.software.sjjh.main.Main;
import nju.software.sjjh.util.Dom4jUtil;
import nju.software.sjjh.util.SecretUtil;
import nju.software.sjjh.util.UuidUtil;

/**
 * 解析参数的实际策略
 * 
 * @author lr12
 * 
 */
@Slf4j
public class HandleBankParam implements HandleParameter {

	@Override
	public List<QueueBank> analyticRequestParameter(String jkbsf, String params) {
		List<QueueBank> queueBanks = new ArrayList<>();
		try {
			Element root = Dom4jUtil.getRooElement(params);
			Map<String, String> rootAttribute = Dom4jUtil.getAttributeMap(root);
			// 银行标识
			String yhbs = rootAttribute.get("FY_QQYHBS");
			String[] banks = yhbs.split("\\|");
			// 优先级
			String yxj = rootAttribute.get("FY_YXJ");
			Element result = root.element("result");
			String deconde = SecretUtil.decode(result.getTextTrim());
			Element decodeElement = Dom4jUtil.getRooElement(deconde);
			Map<String, String> map = Dom4jUtil.getAttributeMap(decodeElement);
			// 请求流水号
			String qqlsh = map.get("FY_QQLSH");
			Iterator<Element> zrrIterator = Dom4jUtil
					.getCurrentNameIeraIterator(decodeElement, "zrr");
			Iterator<Element> zzjgIterator = Dom4jUtil
					.getCurrentNameIeraIterator(decodeElement, "zzjg");
			while (zrrIterator.hasNext()) {
				Element zrr = zrrIterator.next();
				Map<String, String> zrrMap = Dom4jUtil.getAttributeMap(zrr);
				for (String bank : banks) {
					QueueBank queueBank = new QueueBank(
							UuidUtil.generateUuid(), jkbsf, qqlsh, null,
							zrrMap.get("FY_BS"),
							BankService.STATUS_RECEIVE_REQUEST, "江西高院", bank,
							new Date(), null, null, null, zrr.asXML(), null,
							Integer.valueOf(yxj));
					queueBanks.add(queueBank);
				}
			}
			while (zzjgIterator.hasNext()) {
				Element zzjg = zzjgIterator.next();
				Map<String, String> zzjgMap = Dom4jUtil.getAttributeMap(zzjg);
				for (String bank : banks) {
					QueueBank queueBank = new QueueBank(
							UuidUtil.generateUuid(), jkbsf, qqlsh, null,
							zzjgMap.get("FY_BS"),
							BankService.STATUS_RECEIVE_REQUEST, "江西高院", bank,
							new Date(), null, null, null, zzjg.asXML(), null,
							Integer.valueOf(yxj));
					queueBanks.add(queueBank);
				}
			}
		} catch (DocumentException e) {
			log.error("解析xml出错:" + params, e);
			// log.error(e.toString());
			e.printStackTrace();
		}

		return queueBanks;
	}

	@Override
	public List<QueueBank> analyticResponseParameter(String jkbsf,
			String params, QueueBankDao queueBankDao) {
		List<QueueBank> queueBankList=new ArrayList<>();
		try {
			Element responseElement = Dom4jUtil.getRooElement(params);

			Element paramElement = responseElement.element("param");
			if (paramElement == null ) {
				log.error("银行返回的xml有问题" + params);
				return queueBankList;
			}
			Map<String, String> paramAttributeMap=Dom4jUtil.getAttributeMap(paramElement);
			//银行标识
			String yhbs=paramAttributeMap.get("YH_BS");
			Element message = responseElement.element("result");
			if (message != null && message.getTextTrim() != null) {
				String decodeMessageString = SecretUtil.decode(message
						.getTextTrim());
				Element messageElement=Dom4jUtil.getRooElement(decodeMessageString);
				Map<String, String> map = Dom4jUtil
						.getAttributeMap(messageElement);
				//获取任务流水号
				String rwlsl=map.get("FY_RWLSH");
				//获取答复书地址
				String dfsdz=map.get("YH_DFSDZ");
				Iterator<Element> zrrIterator = Dom4jUtil
						.getCurrentNameIeraIterator(messageElement, "zrr");
				Iterator<Element> zzjgIterator = Dom4jUtil
						.getCurrentNameIeraIterator(messageElement, "zzjg");
				Date receiveDate=new Date();
				while (zrrIterator.hasNext()) {
					Element zrr = zrrIterator.next();
					Map<String, String> zrrMap = Dom4jUtil.getAttributeMap(zrr);
					//根据查询标识和银行标识获取请求队列
					List<QueueBank> queueBanks=queueBankDao.getRequestByQueryIdAndResponseId( zrrMap.get("FY_BS"),yhbs);
					if(queueBanks!=null&&queueBanks.size()!=0){
						QueueBank queueBank=queueBanks.get(0);
						queueBank.setDecodedResult(zrr.asXML());
						queueBank.setReceiveResponseTime(receiveDate);
						queueBank.setResponseId(rwlsl);
						queueBankList.add(queueBank);
					}
					
				}
				while (zzjgIterator.hasNext()) {
					Element zzjg = zzjgIterator.next();
					Map<String, String> zzjgMap = Dom4jUtil
							.getAttributeMap(zzjg);
					//根据查询标识和银行标识获取请求队列
					List<QueueBank> queueBanks=queueBankDao.getRequestByQueryIdAndResponseId( zzjgMap.get("FY_BS"),yhbs);
					if(queueBanks!=null&&queueBanks.size()!=0){
						QueueBank queueBank=queueBanks.get(0);
						queueBank.setDecodedResult(zzjg.asXML());
						queueBank.setReceiveResponseTime(receiveDate);
						queueBank.setResponseId(rwlsl);
						queueBankList.add(queueBank);
					}
				}
			}
		} catch (DocumentException e) {
			log.error("解析银行xml失败",e);
			e.printStackTrace();
		}

		return queueBankList;
	}

}
