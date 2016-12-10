package com.net.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import sun.misc.BASE64Decoder;

public class Common {

	/**
	 * 产生随机字符串
	 */
	public static String randString(int length) {
		Random r = new Random();
		String ssource = "0123456789";
		char[] src = ssource.toCharArray();
		char[] buf = new char[length];
		int rnd;
		for (int i = 0; i < length; i++) {
			rnd = Math.abs(r.nextInt()) % src.length;
			buf[i] = src[rnd];
		}
		return new String(buf);
	}
	/**
	 * base64图片解码
	 * @param imgStr
	 * @return
	 */
	public String GenerateImage(String imgStr)
    {//对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return "";
        BASE64Decoder decoder = new BASE64Decoder();
        try 
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "headimg/"+System.currentTimeMillis()+".jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);    
            out.write(b);
            out.flush();
            out.close();
            return imgFilePath;
        } 
        catch (Exception e) 
        {
            return "";
        }
    }

	
}
