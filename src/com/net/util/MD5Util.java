package com.net.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import sun.misc.BASE64Encoder;

import java.util.Arrays;  

import javax.crypto.KeyGenerator;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.IvParameterSpec;

public class MD5Util {
	
	private static Cipher cipher;  
	private static final String KEY_ALGORITHM = "AES";  
	private static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";  
	private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
	
	static SecretKey secretKey;


//	private static String byteArrayToHexString(byte b[]) {
//		StringBuffer resultSb = new StringBuffer();
//		for (int i = 0; i < b.length; i++)
//			resultSb.append(byteToHexString(b[i]));
//
//		return resultSb.toString();
//	}
//
//	private static String byteToHexString(byte b) {
//		int n = b;
//		if (n < 0)
//			n += 256;
//		int d1 = n / 16;
//		int d2 = n % 16;
//		return hexDigits[d1] + hexDigits[d2];
//	}
//
//	public static String MD5Encode(String origin, String charsetname) {
//		String resultString = null;
//		try {
//			resultString = new String(origin);
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			if (charsetname == null || "".equals(charsetname))
//				resultString = byteArrayToHexString(md.digest(resultString
//						.getBytes()));
//			else
//				resultString = byteArrayToHexString(md.digest(resultString
//						.getBytes(charsetname)));
//		} catch (Exception exception) {
//		}
//		return resultString;
//	}
//
//	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
//			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
//
//	public static String Sign(String content, String md5Key) {
//        try {
//            return Sign(content, md5Key,"GBK");
//        } catch (IllegalArgumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String Sign(String content, String md5Key,String charset)
//            throws IllegalArgumentException {
//        String signStr = "";
//
//        if ("" == md5Key) {
//            throw new IllegalArgumentException("财付通签名key不能为空！");
//        }
//        if ("" == content) {
//            throw new IllegalArgumentException("财付通签名内容不能为空");
//        }
//        signStr = content + "&key=" + md5Key;
//
//        return MD5Util.MD5(signStr,charset).toUpperCase();
//
//    }
    public final static String MD5(String s,String charset) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes(charset);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
       }
    }
    
    
	/**
	 * md5加密
	 * @param desc
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encodingForMd5(String desc) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		BASE64Encoder base64 = new BASE64Encoder();
		byte[] md5bytes = md.digest(desc.getBytes());
		String md5 = base64.encode(md5bytes);
		return md5;
	} 
    
	public static String encryption(String plain) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plain.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}
	
	
	
	public static String string2MD5(String plainText){    
        String re_md5 = new String();  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(plainText.getBytes());  
            byte b[] = md.digest();  
   
            int i;  
   
            StringBuffer buf = new StringBuffer("");  
            for (int offset = 0; offset < b.length; offset++) {  
                i = b[offset];  
                if (i < 0)  
                    i += 256;  
                if (i < 16)  
                    buf.append("0");  
                buf.append(Integer.toHexString(i));  
            }  
   
            re_md5 = buf.toString();  
   
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        return re_md5;  
    }
	
	  public static String sign(byte[] priKeyText, String plainText) {  
	        try {     
	            String strPriKeyText = new String(priKeyText);  
	            byte[] decodeData = Base64.decode(strPriKeyText);   
	            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(decodeData);     
	            KeyFactory keyf = KeyFactory.getInstance("RSA");     
	            PrivateKey prikey = keyf.generatePrivate(priPKCS8);   //用私钥对信息生成数字签名     
	            java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");     
	            signet.initSign(prikey);     
	            signet.update(plainText.getBytes());     
	            String b64str = new String(Base64.encode(signet.sign()));  
	               
	            return b64str;    
	            } catch (java.lang.Exception e) {    
	                System.out.println("签名失败");     
	                e.printStackTrace();    
	            }  return null;   
	        } 
	
	  
	  public static String getBase64(String str) {    
	        byte[] b = null;    
	        String s = null;    
	        try {    
	            b = str.getBytes("utf-8");    
	        } catch (UnsupportedEncodingException e) {    
	            e.printStackTrace();    
	        }    
	        if (b != null) {    
	            s = new BASE64Encoder().encode(b);    
	        }    
	        return s;    
	    }
	  
//	  public  static void method3(String str) throws Exception {  
//	        cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);  
//	        //KeyGenerator 生成aes算法密钥  
//	        secretKey = KeyGenerator.getInstance(KEY_ALGORITHM).generateKey();  
//	        System.out.println("密钥的长度为：" + secretKey.getEncoded().length);  
//	          
//	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用加密模式初始化 密钥  
//	        byte[] encrypt = cipher.doFinal(str.getBytes()); //按单部分操作加密或解密数据，或者结束一个多部分操作。  
//	          
//	        System.out.println("method3-加密：" + Arrays.toString(encrypt));  
//	        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用解密模式初始化 密钥  
//	        byte[] decrypt = cipher.doFinal(encrypt);  
//	        System.out.println("method3-解密后：" + new String(decrypt));  
	      
    

	
    public static void main(String arg[]) throws NoSuchAlgorithmException{
    	//7b9cbbfe355d97cc5de5cb9d3f3ac62f
//    	String  sss  = "JH81f8c5a2e5b2ff903b8b984665560d2b6415700110001000114200004926960902015092404691247";
//    	System.out.println(MD5(sss,"UTF-8").toLowerCase());
//    	System.out.println(encodingForMd5("handsome"));
//    	System.out.println(encryption("handsome"));
//    	System.out.println(MD5("handsome","utf-8"));
    	System.out.println(string2MD5("handsome"));
    	System.out.println(getBase64("handsome"));
    }

}
