package com.net.jfinal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
/**
 * JFinal路由配置策略
 * 
 * huilet 2013-5-14
 * @author yuanc
 */
public class JFinalMy extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		// TODO Auto-generated method stub
//		int index = target.lastIndexOf("services");
//		if (index != -1)
//			isHandled[0] = false;
		if("/".equals(target)){
			isHandled[0] = false;
		}
	}

}
