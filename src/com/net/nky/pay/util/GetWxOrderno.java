 package com.net.nky.pay.util;
 
 import java.io.ByteArrayInputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.PrintStream;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import org.apache.http.HttpResponse;
 import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
 import org.apache.http.entity.StringEntity;
 import org.apache.http.impl.client.DefaultHttpClient;
 import org.apache.http.params.HttpParams;
 import org.apache.http.util.EntityUtils;
 import org.jdom.Document;
 import org.jdom.Element;
 import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.net.nky.pay.http.HttpClientConnectionManager;
 
 public class GetWxOrderno
 {
	 	public static DefaultHttpClient httpclient;

	    static 
	    {
	    	httpclient = new DefaultHttpClient();
	        httpclient = (DefaultHttpClient)HttpClientConnectionManager.getSSLInstance(httpclient);
	    }
   public static String getPayNo(String url, String xmlParam)
   {
     System.out.println("xml是:" + xmlParam);
     DefaultHttpClient client = new DefaultHttpClient();
     client.getParams().setParameter("http.protocol.allow-circular-redirects", Boolean.valueOf(true));
     HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
     String prepay_id = "";
     try {
       httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
       HttpResponse response = httpclient.execute(httpost);
       String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
       Map dataMap = new HashMap();
       System.out.println("json是:" + jsonStr);
 
       if (jsonStr.indexOf("FAIL") != -1) {
         return prepay_id;
       }
       Map map = doXMLParse(jsonStr);
       String return_code = (String)map.get("return_code");
       prepay_id = (String)map.get("prepay_id");
     }
     catch (Exception e) {
       e.printStackTrace();
     }
     return prepay_id;
   }
 
   public static Map doXMLParse(String strxml)
   {
     try
     {
       if ((strxml == null) || ("".equals(strxml))) {
         return null;
       }
 
       Map m = new HashMap();
       InputStream in = String2Inputstream(strxml);
       SAXBuilder builder = new SAXBuilder();
 
       Document doc = builder.build(in);
 
       Element root = doc.getRootElement();
       List list = root.getChildren();
       Iterator it = list.iterator();
       while (it.hasNext()) {
         Element e = (Element)it.next();
         String k = e.getName();
         String v = "";
         List children = e.getChildren();
         if (children.isEmpty())
           v = e.getTextNormalize();
         else {
           v = getChildrenText(children);
         }
 
         m.put(k, v);
       }
 
       in.close();
 
       return m;
     }
     catch (IOException e1) {
       e1.printStackTrace();
     } catch (JDOMException e) {
		e.printStackTrace();
	}return null;
   }
 
   public static String getChildrenText(List children)
   {
     StringBuffer sb = new StringBuffer();
     if (!children.isEmpty()) {
       Iterator it = children.iterator();
       while (it.hasNext()) {
         Element e = (Element)it.next();
         String name = e.getName();
         String value = e.getTextNormalize();
         List list = e.getChildren();
         sb.append("<" + name + ">");
         if (!list.isEmpty()) {
           sb.append(getChildrenText(list));
         }
         sb.append(value);
         sb.append("</" + name + ">");
       }
     }
 
     return sb.toString();
   }
   public static InputStream String2Inputstream(String str) {
     return new ByteArrayInputStream(str.getBytes());
   }

	  public static String getCodeUrl(String url,String xmlParam){
		  DefaultHttpClient client = new DefaultHttpClient();
		  client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		  HttpPost httpost= HttpClientConnectionManager.getPostMethod(url);
		  String code_url = "";
	     try {
			 httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			 HttpResponse response = GetWxOrderno.httpclient.execute(httpost);
		     String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
		    if(jsonStr.indexOf("FAIL")!=-1){
		    	return code_url;
		    }
		    Map map = GetWxOrderno.doXMLParse(jsonStr);
		    code_url  = (String) map.get("code_url");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code_url;
	  }
	  
 }

/* Location:           
 * Qualified Name:     com.yq.weixin.pay.util.GetWxOrderno
 * JD-Core Version:    0.6.2
 */