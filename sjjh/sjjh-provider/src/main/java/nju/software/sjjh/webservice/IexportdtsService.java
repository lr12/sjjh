package nju.software.sjjh.webservice;

public interface IexportdtsService {

	/**
	 * 导出法庭信息
	 * @param xml
	 * @return
	 */
	public String exportFTXX(String xml);
	
	/**
	 * 导出人员信息
	 * @param xml
	 * @return
	 */
	public String exportRYXX(String xml);
	
	/**
	 * 导出开庭信息
	 * @param xml
	 * @return
	 */
	public String exportKTXX(String xml);
	
	
	/**
	 * 导出案件经办信息
	 * @param xml
	 * @return
	 */
	public String exportAJJBXX(String xml);
	
	/**
	 * 导出当事人信息
	 * @param xml
	 * @return
	 */
	public String exportDSRXX(String xml);
	
	/**
	 * 导出审判组织成员信息
	 * @param xml
	 * @return
	 */
	public String exportSPZZCYXX(String xml);
	
	
	/**
	 * 回写笔录信息接口
	 * @param xml
	 * @return
	 */
	public String updateTrialInfo(String xml);
	
	
	/**
	 * 获取庭审录像接口
	 * @param xml
	 * @return
	 */
	public String getCaseVod(String xml);
	
	/**
	 * 测试
	 * @return
	 */
	public String sayHello(String xml);
}
