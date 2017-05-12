package nju.software.sjjh.sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;

public class Base64Util {
	public static String encode(String str){
		Base64 coder=new Base64();
		if(str==null){
			return "";
		}
		try{
			return new String(coder.encode(str.getBytes()));
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	public static String decode(String str){
		Base64 coder=new Base64();
		if(str==null){
			return "";
		}
		try{
			return new String(coder.decode(str.getBytes()),"utf-8");
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Base64����
	 * @param b
	 * @return
	 */
	public static String getBASE64(byte[] b){
		String s = null;
		
		if (b != null){
			s = new sun.misc.BASE64Encoder().encode(b);
		}
		return s;
	}
	
	/**
	 * Base64����
	 * @param s
	 * @return
	 */
	public static byte[] getFromBASE64(String s){
		byte[] b = null;
		
		if (s != null){
			s=s.trim();
			BASE64Decoder decoder = new BASE64Decoder();
			
			try{
				b = decoder.decodeBuffer(s);
				return b;
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return b;
	}
	
	/**
	 * Base64���ܲ�����
	 * @param b
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getBASE64String(byte[] b){
		String s = null;
		
		if (b != null){
			s = new String(new Base64().encodeBase64(b));
		}
		
		return s;
	}
	
//	public static void main(String args[]) throws Exception{
//		/*String test="����и߼�����Ժ";
////		String test="loginId=test&type=1&ajlb=1,2,6&order=1&ajjzjd=1,2&pageNo=2&perPage=10";
//		String str=getBASE64String(test.getBytes("utf-8"));
//		System.out.println(str);
//		System.out.println(new String(getFromBASE64(str),"utf-8"));
//		System.out.println("*****************");
//		str=encode(test);
//		System.out.println(str);
//		System.out.println(decode(str));
//		System.out.println("*****************");
//		String toBeSign="bG9naW5JZD10ZXN0JnR5cGU9MSZhamxiPTEsMiw2Jm9yZGVyPTEmYWpqempkPTEsMiZw"
//				+ "YWdlTm89MiZwZXJQYWdlPTEw211bfa7efbcbe28431ceb328969cb15e";
//		MD5Signature signature=new MD5Signature();
//		System.out.println(signature.sign(toBeSign, "211bfa7efbcbe28431ceb328969cb15e", "utf-8").toLowerCase());*/
////		String content="bG9naW5JZD10ZXN0JnR5cGU9MSZhamxiPTEsMiw2Jm9yZGVyPTEmYWpqempkPTEsMiZwYWdlTm89MiZwZXJQYWdlPTEw";
//		String content="";
//		String sign="8d35b1c8c063e8d96ad1e6b279e5209f";
//		String key="211bfa7efbcbe28431ceb328969cb15e";
//		String charset="utf-8";
//		MD5Signature signature=new MD5Signature();
//		String encode=signature.sign(content, key, charset);
//		System.out.println(encode);
////		System.out.println(signature.check(content, sign, key, charset));
//	}
	
	  private static byte[] base64DecodeChars = { -1, -1, -1, -1, -1, 
		    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
		    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
		    -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 
		    60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
		    10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, 
		    -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 
		    38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, 
		    -1, -1 };
	
	  public static byte[] decode1(String str)
			    throws UnsupportedEncodingException
			  {
			    StringBuffer sb = new StringBuffer();
			    byte[] data = str.getBytes("UTF-8");
			    int len = data.length;
			    int i = 0;
			    while (i < len)
			    {
			      int b1;
			      do
			      {
			        b1 = base64DecodeChars[data[(i++)]];
			      } while ((i < len) && (b1 == -1));
			      if (b1 == -1) {
			        break;
			      }
			      int b2;
			      do
			      {
			        b2 = base64DecodeChars[data[(i++)]];
			      } while ((i < len) && (b2 == -1));
			      if (b2 == -1) {
			        break;
			      }
			      sb.append((char)(b1 << 2 | (b2 & 0x30) >>> 4));
			      int b3;
			      do
			      {
			        b3 = data[(i++)];
			        if (b3 == 61) {
			          return sb.toString().getBytes("iso8859-1");
			        }
			        b3 = base64DecodeChars[b3];
			      } while ((i < len) && (b3 == -1));
			      if (b3 == -1) {
			        break;
			      }
			      sb.append((char)((b2 & 0xF) << 4 | (b3 & 0x3C) >>> 2));
			      int b4;
			      do
			      {
			        b4 = data[(i++)];
			        if (b4 == 61) {
			          return sb.toString().getBytes("iso8859-1");
			        }
			        b4 = base64DecodeChars[b4];
			      } while ((i < len) && (b4 == -1));
			      if (b4 == -1) {
			        break;
			      }
			      sb.append((char)((b3 & 0x3) << 6 | b4));
			    }
			    return sb.toString().getBytes("iso8859-1");
			  }
	
	
	  private static char[] base64EncodeChars = { 'A', 'B', 'C', 'D', 
		    'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 
		    'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
		    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 
		    'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', 
		    '4', '5', '6', '7', '8', '9', '+', '/' };
	
	public static String encode1(byte[] data)
	  {
	    StringBuffer sb = new StringBuffer();
	    int len = data.length;
	    int i = 0;
	    while (i < len)
	    {
	      int b1 = data[(i++)] & 0xFF;
	      if (i == len)
	      {
	        sb.append(base64EncodeChars[(b1 >>> 2)]);
	        sb.append(base64EncodeChars[((b1 & 0x3) << 4)]);
	        sb.append("==");
	        break;
	      }
	      int b2 = data[(i++)] & 0xFF;
	      if (i == len)
	      {
	        sb.append(base64EncodeChars[(b1 >>> 2)]);
	        sb.append(base64EncodeChars[
	          ((b1 & 0x3) << 4 | (b2 & 0xF0) >>> 4)]);
	        sb.append(base64EncodeChars[((b2 & 0xF) << 2)]);
	        sb.append("=");
	        break;
	      }
	      int b3 = data[(i++)] & 0xFF;
	      sb.append(base64EncodeChars[(b1 >>> 2)]);
	      sb.append(base64EncodeChars[
	        ((b1 & 0x3) << 4 | (b2 & 0xF0) >>> 4)]);
	      sb.append(base64EncodeChars[
	        ((b2 & 0xF) << 2 | (b3 & 0xC0) >>> 6)]);
	      sb.append(base64EncodeChars[(b3 & 0x3F)]);
	    }
	    return sb.toString();
	  }
		
	 public static void main(String[] args) throws Exception
	  {
		 String zipcode=getFileByteString(new File("C:\\Users\\dell1\\Desktop\\jumpToHighCourt.zip"));
		 System.out.println(zipcode);
         byte[] bytes = Base64Util.decode1(zipcode);
         OutputStream out = new FileOutputStream("C:\\Users\\dell1\\Desktop\\test.zip");
         out.write(bytes);
         out.flush();
         out.close();
	  }
	
	 private static String getFileByteString(File file)
			    throws Exception
			  {
			    InputStream in = new FileInputStream(file);
			    

			    long length = file.length();
			    
			    byte[] bytes = new byte[(int)length];
			    
			    int offset = 0;
			    int numRead = 0;
			    while ((offset < bytes.length) && (
			      (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0)) {
			      offset += numRead;
			    }
			    if (offset < bytes.length) {
			      throw new IOException("������ȫ��ȡ�ļ���" + file.getName());
			    }
			    in.close();
			    
			    String encodedFileString = Base64Util.encode1(bytes);
			    return encodedFileString;
			  }


}
