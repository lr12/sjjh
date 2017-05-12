/**
 * created by 2010-6-28
 */
package nju.software.sjjh.sign;

import java.security.SignatureException;

/**
 * ǩ��ӿ�
 * @author zym
 *
 */
public interface Signature {
	/**
     * �ⲿ�ӿڲ���ǩ��<p>
     * 
     * ʹ��privateKey��ԭʼ��ݽ���ǩ��
     * 
     * @param content ԭʼ���
     * @param privateKey ˽Կ
     * @param charset ���뼯
     * @return ǩ�����
     * @return
	 * @throws SignatureException 
     */
    public String sign(String content, String privateKey, String charSet) throws SignatureException;

    /**
     * * ��֤ǩ��
     * 
     * @param content ԭʼ���
     * @param signature ǩ�����
     * @param publicKey ��Կ
     * @param charset ���뼯
     * @return True ǩ����֤ͨ�� False ǩ����֤ʧ��
     * 
     */
    public boolean check(String content, String signature, String publicKey, String charset) throws SignatureException;
}
