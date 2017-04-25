package nju.software.sjjh.strategy;

import java.util.List;

import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.QueueBank;

public class HandleBankParam implements HandleParameter {

	@Override
	public List<QueueBank> analyticRequestParameter(String params) {
	
		
		return null;
	}

	@Override
	public List<QueueBank> analyticResponseParameter(String params,
			QueueBankDao queueBankDao) {
		// TODO Auto-generated method stub
		return null;
	}

}
