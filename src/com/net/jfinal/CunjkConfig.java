package com.net.jfinal;

import java.util.HashMap;

import com.alibaba.druid.filter.stat.StatFilter;
import com.huilet.util.FileUtil;
import com.huilet.util.xml.ReadXml;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.net.ServiceConstants;

/**
 * API引导式配置
 * @author yuanhuaihao
 * company huilet
 * 2013-3-12
 */
public class CunjkConfig extends JFinalConfig{
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		me.setDevMode(true);
		me.setViewType(ViewType.JSP);  // 设置视图类型为Jsp，否则默认为FreeMarker
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		MyRoutesUtil.add(me);
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(ServiceConstants.jdbc_url, ServiceConstants.jdbc_user,  ServiceConstants.jdbc_password);
		me.add(c3p0Plugin);
		AutoTableBindPlugin autoTableBindPlugin = new AutoTableBindPlugin(c3p0Plugin, TableNameStyle.UP);
		autoTableBindPlugin.setShowSql(true);
		autoTableBindPlugin.setContainerFactory(new CaseInsensitiveContainerFactory());
		me.add(autoTableBindPlugin);
		

	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new JFinalMy());
	}

}
