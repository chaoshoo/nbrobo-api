package com.net.nky.service.cache;

import java.util.Properties;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

public class MessageCacheEventListenerFactory extends CacheEventListenerFactory {

	@Override  
	   public CacheEventListener createCacheEventListener(Properties properties) {  
	      return new MessageCacheEventListener();  
	   }

}