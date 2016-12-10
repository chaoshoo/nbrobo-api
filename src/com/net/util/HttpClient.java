package com.net.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;



public class HttpClient {
	
	
	public static String httpPost(String url, Map<String, String> parms)
			throws IOException {
		URL postUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) postUrl
				.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.connect();
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());
		out.writeBytes(mapToLink(parms));
		out.flush();
		out.close();
		String str = readInputStream(connection.getInputStream());
		// reader.close();
		connection.disconnect();
		return str;
	}
	
	public static String readInputStream(InputStream inStream) {
		String dataStr = null;
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			dataStr = new String(outStream.toByteArray(), "utf-8");
			outStream.close();
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataStr;
	}
	
	public static String mapToLink(Map<String, String> map) {
		if (map == null) {
			return "";
		}
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = map.get(key);
			if (StringUtils.isNotEmpty(value)) {// 去除空值
				try {
					prestr = prestr + "&" + key + "=" + java.net.URLEncoder.encode(value , "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return prestr.substring(1);
	}
	
	public static  String PushForHttps(String content,String url) {
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码 
		try {
			String contens = URLEncoder.encode(content, "UTF-8");
			String getURL = url + "?content="+contens; 
			URL getUrl = new URL(getURL); 
			System.out.println("request---->"+getURL);
		// 根据拼凑的URL，打开连接，URL.openConnection()函数会根据 URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection 
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			// 建立与服务器的连接，并未发送数据 
			connection.connect();
			// 发送数据到服务器并使用Reader读取返回的数据 
			 BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8")); 
			 String lines; //定义返回值
			 String result = "";
			while ((lines = reader.readLine()) != null) {
				result+=lines;
			}
			// 断开连接
			reader.close();
			connection.disconnect(); 
			System.out.println("---->"+result);
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "";
	}
	
	
	
	 public static String doGet(String url, String charset)
		      throws Exception {
		    /*
		     * 使用 GetMethod 来访问一个 URL 对应的网页,实现步骤: 1:生成一个 HttpClinet 对象并设置相应的参数。
		     * 2:生成一个 GetMethod 对象并设置响应的参数。 3:用 HttpClinet 生成的对象来执行 GetMethod 生成的Get
		     * 方法。 4:处理响应状态码。 5:若响应正常，处理 HTTP 响应内容。 6:释放连接。
		     */
		    /* 1 生成 HttpClinet 对象并设置参数 */
		 org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
		    // 设置 Http 连接超时为5秒
		    httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		    /* 2 生成 GetMethod 对象并设置参数 */
		    GetMethod getMethod = new GetMethod(url);
		    // 设置 get 请求超时为 5 秒
		    getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		    // 设置请求重试处理，用的是默认的重试处理：请求三次
		    getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler());
		    String response = "";
		    /* 3 执行 HTTP GET 请求 */
		    try {
		      int statusCode = httpClient.executeMethod(getMethod);
		      /* 4 判断访问的状态码 */
		      if (statusCode != HttpStatus.SC_OK) {
		        System.err.println("请求出错: "+ getMethod.getStatusLine());
		      }
		      /* 5 处理 HTTP 响应内容 */
		      // HTTP响应头部信息，这里简单打印
		      Header[] headers = getMethod.getResponseHeaders();
		      for (Header h : headers)
		        System.out.println(h.getName() + "------------ " + h.getValue());
		      // 读取 HTTP 响应内容，这里简单打印网页内容
		      byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
		      response = new String(responseBody, charset);
		      System.out.println("----------response:" + response);
		      // 读取为 InputStream，在网页内容数据量大时候推荐使用
		      // InputStream response = getMethod.getResponseBodyAsStream();
		    } catch (HttpException e) {
		      // 发生致命的异常，可能是协议不对或者返回的内容有问题
		      System.out.println("请检查输入的URL!");
		      e.printStackTrace();
		    } catch (IOException e) {
		      // 发生网络异常
		      System.out.println("发生网络异常!");
		      e.printStackTrace();
		    } finally {
		      /* 6 .释放连接 */
		      getMethod.releaseConnection();
		    }
		    return response;
		  }
	 
	 
	 public  static String postForJson(String url, String parameters) throws Exception {
			PrintWriter out = null;
			BufferedReader in = null;
			String result = "";
			URLConnection conn = null;
			try {
				URL realUrl = new URL(url);
				// 打开和URL之间的连接
				conn = realUrl.openConnection();

				// 设置通用的请求属性
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type",
						"application/json; charset=UTF-8");
				conn.setRequestProperty("user-agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				// 发送POST请求必须设置如下两行
				conn.setDoOutput(true);
				conn.setDoInput(true);
				// 获取URLConnection对象对应的输出流
				out = new PrintWriter(conn.getOutputStream());
				// 发送请求参数
				out.print(parameters);
				// flush输出流的缓冲
				out.flush();
				// 定义BufferedReader输入流来读取URL的响应

				in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8")); // 去掉了UTF-8
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("发送 POST 请求出现异常 !" + e);
			}
			// 使用finally块来关闭输出流、输入流
			finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}

				} catch (IOException ex) {
					ex.printStackTrace();
					throw new Exception("发送 POST 请求出现异常 !" + ex);
				}
			}
			return result;
		}
	
	 
	 public static void main(String[] args) throws Exception {
		String json=doGet("http://27.17.40.149:9000/hy_ghservice/centergh/jiami?miwen=abfdah", "utf-8");
		System.out.println(json);
		json=json.substring(json.indexOf("是\"")+4, json.length()-2);
		System.out.println(json);
		
	}
	 
	
	
}