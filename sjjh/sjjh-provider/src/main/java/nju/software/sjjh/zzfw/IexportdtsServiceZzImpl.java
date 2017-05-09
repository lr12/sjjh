package nju.software.sjjh.zzfw;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import com.alibaba.dubbo.config.annotation.Service;

import nju.software.sjjh.webservice.IexportdtsService;
@Slf4j
@Service(protocol = {"webservice"})
public class IexportdtsServiceZzImpl implements IexportdtsServiceZz{
	  @Resource
	IexportdtsService iexportdtsService;
	
	

	@Override
	public String exportFTXX(String xml) {
		
		return iexportdtsService.exportFTXX(xml);
	}

	@Override
	public String exportRYXX(String xml) {
		return iexportdtsService.exportRYXX(xml);
	}

	@Override
	public String exportKTXX(String xml) {

		return iexportdtsService.exportKTXX(xml);
	}

	@Override
	public String exportAJJBXX(String xml) {
	
		return iexportdtsService.exportAJJBXX(xml);
	}

	@Override
	public String exportDSRXX(String xml) {
		
		return iexportdtsService.exportDSRXX(xml);
	}

	@Override
	public String exportSPZZCYXX(String xml) {
	
		return iexportdtsService.exportSPZZCYXX(xml);
	}

	@Override
	public String updateTrialInfo(String xml) {
		
		return iexportdtsService.updateTrialInfo(xml);
	}

	@Override
	public String getCaseVod(String xml) {
		
		return iexportdtsService.getCaseVod(xml);
	}

	@Override
	public String sayHello(String xml) {
	//	System.out.println(iexportdtsService);
		return iexportdtsService.sayHello(xml);
	}

}
