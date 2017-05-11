package nju.software.sjjh.webservice;


import lombok.extern.slf4j.Slf4j;


import com.alibaba.dubbo.config.annotation.Service;

/**
 * 科技法庭接口实现
 * @author lr12
 *
 */
@Service(protocol = {"webservice"})
@Slf4j
public class IexportdtsServiceImpl implements IexportdtsService {


	
	@Override
	public String exportFTXX(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportRYXX(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportKTXX(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportAJJBXX(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportDSRXX(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportSPZZCYXX(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateTrialInfo(String xml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCaseVod(String xml) {
		//String result=null;
		
		return null;
	}

	@Override
	public String sayHello(String xml) {
	   // System.out.println(xml);
		return "hello";
	}

}
