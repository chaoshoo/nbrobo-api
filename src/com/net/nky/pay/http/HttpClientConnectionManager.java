 package com.net.nky.pay.http;
 
 import org.apache.http.client.HttpClient;
 import org.apache.http.client.methods.HttpGet;
 import org.apache.http.client.methods.HttpPost;
 import org.apache.http.conn.ClientConnectionManager;
 import org.apache.http.conn.scheme.Scheme;
 import org.apache.http.conn.scheme.SchemeRegistry;
 import org.apache.http.impl.client.DefaultHttpClient;
 
 public class HttpClientConnectionManager
 {
   public static HttpClient getSSLInstance(HttpClient httpClient)
   {
     ClientConnectionManager ccm = httpClient.getConnectionManager();
     SchemeRegistry sr = ccm.getSchemeRegistry();
     sr.register(new Scheme("https", MySSLSocketFactory.getInstance(), 443));
     httpClient = new DefaultHttpClient(ccm, httpClient.getParams());
     return httpClient;
   }
 
   public static HttpPost getPostMethod(String url)
   {
     HttpPost pmethod = new HttpPost(url);
     pmethod.addHeader("Connection", "keep-alive");
     pmethod.addHeader("Accept", "*/*");
     pmethod.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
     pmethod.addHeader("Host", "api.mch.weixin.qq.com");
     pmethod.addHeader("X-Requested-With", "XMLHttpRequest");
     pmethod.addHeader("Cache-Control", "max-age=0");
     pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
     return pmethod;
   }
 
   public static HttpGet getGetMethod(String url)
   {
     HttpGet pmethod = new HttpGet(url);
 
     pmethod.addHeader("Connection", "keep-alive");
     pmethod.addHeader("Cache-Control", "max-age=0");
     pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
     pmethod.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/;q=0.8");
     return pmethod;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.weixin.pay.http.HttpClientConnectionManager
 * JD-Core Version:    0.6.2
 */