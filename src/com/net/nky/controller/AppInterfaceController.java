package com.net.nky.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.net.nky.service.AppInterfaceService;

/**
 * 宁康园对外接口.
 * Demo:
 * http://localhost:8080/nkyapi/mobile/interface.do?content={type:%27login%27}
 * 
 * @author Ken
 * @version 2016年8月19日
 */
@Controller
@RequestMapping("/mobile")
public class AppInterfaceController {

	private static final Logger LOG = LoggerFactory.getLogger(AppInterfaceController.class);

	@Autowired
	protected AppInterfaceService appInterfaceService;

	@RequestMapping(value = "/interface.do")
	public void Interface(HttpServletRequest request, HttpServletResponse response) {
		try {
			long a = System.currentTimeMillis();
			// 返回的结果对象
			JSONObject result = new JSONObject();
			// 获取参数
			String content = request.getParameter("content");

			LOG.info("request------->" + content);
			JSONObject messageObj = JSONObject.fromObject(URLDecoder.decode(content.replaceAll("%", "%25"), "utf-8"));
			String type = (String) messageObj.getString("type").toLowerCase();
			//result.put("type", type);
			Class clz = null;
			try {
				clz = appInterfaceService.getClass();
				Method thd = clz.getMethod(type, messageObj.getClass(), result.getClass());
				thd.invoke(clz.newInstance(), new Object[] { messageObj, result });
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/json;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(result.toString());
			LOG.info("response--->" + result.toString());
			LOG.info("----------[" + type + "] execute time  ---->" + (System.currentTimeMillis() - a) / 1000f + "s");
		} catch (IOException e) {
			try {
				response.getWriter().write(e.getMessage());
			} catch (IOException e1) {
				LOG.error("关闭输出流异常.", e1);
			}
			LOG.error("IO异常.", e);
		}
	}

}
