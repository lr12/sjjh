/**
 * created by 2010-6-28
 */
package nju.software.sjjh.sign;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5ǩ��ߵ�ʵ��
 * @author zym
 *
 */
public class MD5Signature implements Signature {

	/* (non-Javadoc)
	 * @see nju.edu.software.cooperate.sign.Signature#sign(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String sign(String content, String privateKey, String charset) throws SignatureException {
		// TODO Auto-generated method stub
        String tosign;
        if (content == null)
            tosign = privateKey;
        else
            tosign = content + privateKey;
        try {
            String sign = DigestUtils.md5Hex(tosign.getBytes(charset));

            return sign;
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("Unsupported Encoding" + e);
        }
	}

	/* (non-Javadoc)
	 * @see nju.edu.software.cooperate.sign.Signature#check(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean check(String content, String signature, String publicKey,
			String charset) throws SignatureException {
		/*if (StringUtil.isBlank(content) || StringUtil.isBlank(publicKey)
	            || StringUtil.isBlank(signature)) {

	            throw new SignatureException("content or publicKey or signature is null!");
	        }*/

	        String tosign = content + publicKey;

	        try {
	            String mySign = DigestUtils.md5Hex(tosign.getBytes(charset));

	            boolean verify = false;
	            if (mySign.equals(signature)) {
	                verify = true;
	            }
	            return verify;
	        } catch (UnsupportedEncodingException e) {
	            throw new SignatureException("Unsupported Encoding",e);
	        }
	}

	
}
