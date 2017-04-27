package nju.software.sjjh.strategy;

import java.util.List;

import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.QueueBank;

/**
 * 解析参数的策略
 * @author lr12
 *
 */
public interface HandleParameter {

	/**
	 * 解析请求的参数方法
	 * @param requestInterfaceId 请求的接口标识
	 * @param responseInterfaceId 回复的接口标识
	 * @param params
	 * @return
	 */
	public List<QueueBank> analyticRequestParameter(String requestInterfaceId,String responseInterfaceId,String params);
	
	
	/**
	 * 解析响应参数的方法
	 * @param params
	 * @param queueBankDao
	 * @return
	 */
	public List<QueueBank> analyticResponseParameter(String params,QueueBankDao queueBankDao);
}
