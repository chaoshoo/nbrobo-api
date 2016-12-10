package com.net.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加解密类
 * @author 涂缘
 *
 */
public class DESUtil {

	/**
	 * 加密，密钥长度都必须是8的倍数 返回16进制字符串
	 * 
	 * @param datasource
	 * @param password
	 * @return
	 */
	private byte[] encrypt(String datasource, String password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(datasource.getBytes("UTF-8"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用DES加密后，返回16进制字符串
	 * @param datasource
	 * @param password
	 * @return
	 */
	public String encryptToHex(String datasource, String password) {
		byte[] dd = encrypt(datasource,password);
		return bytesToHexString(dd);
	}
	/**
	 * 解密
	 * 
	 * @param src
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private byte[] decrypt(byte[] src, String password) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}
	/**
	 * 解密经过DES加密的16进制的字符串
	 * @param hexSrc
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String decryptHex(String hexSrc, String password) {
		byte[] tt = hexStringToByte(hexSrc);

		// 直接将如上内容解密
		try {
			byte[] decryResult = decrypt(tt, password);
			return  new String(decryResult,"UTF-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

	/**
	 * byte数组转换成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 16进制字符串转成byte数组
	 * 
	 * @param src
	 * @return
	 */
	private static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DESUtil des = new DESUtil();
		// 待加密内容
		String str = "测试内容asda可是sdadfasdfaaaf1232323aaaaa2121212&*())__+!@#$%^&~";
		System.out.println("加密前内容为:"+str);
		// 密码，长度要是8的倍数
		String password = "V0QWX6T7MOY921DJUSN4E5GBZRLIKC8AFHP3";
		String hexSrc = des.encryptToHex(str, password);
		System.out.println("加密后内容为：" + hexSrc);

		// 直接将如上内容解密
		try {
			String decryResult = des.decryptHex(hexSrc, password);
			System.out.println("解密后内容为:" + decryResult);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
