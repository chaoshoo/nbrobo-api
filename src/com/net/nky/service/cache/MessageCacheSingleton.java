package com.net.nky.service.cache;

import java.net.URL;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 短信缓存.注意配置是5分钟失效，而且验证码匹配上就失效，未匹配可以继续匹配.
 * @author Ken
 * @version 2016年8月19日
 */
public class MessageCacheSingleton {

	private static MessageCacheSingleton instance = new MessageCacheSingleton();
	private static final Logger LOG = LoggerFactory.getLogger(MessageCacheSingleton.class);
	

    static URL url = MessageCacheSingleton.class.getResource("/ehcache.xml"); 
    static CacheManager cacheManager = CacheManager.create(url);

    // 验证码缓存 10分钟有效
    static Cache varifyCache = cacheManager.getCache("varify");
	
	private MessageCacheSingleton() {
	}

	public static MessageCacheSingleton getSingleton() {
		return instance;
	}

//	/**
//	* 通过名称从缓存中获取数据.
//	* @param tel 手机号码.
//	* @return 没有返回null
//	*/
//	public String get(String tel) {
//		try{
//			net.sf.ehcache.Element e = cache.get(tel);
//			if (e == null) {
//				return null;
//			}
//			String result = (String)e.getObjectValue();
//			LOG.debug(tel+" get cache "+result);
//			return result;
//		}catch(Exception e){
//			return null;
//		}
//	}
//
//	/**
//	 * 将对象添加到缓存中.
//	* @param tel 手机号码.
//	 */
//	public void set(String tel, String result) {
//		LOG.debug(tel+" ready to cache set "+result);
//		Element element = new Element(tel, result);
//		cache.put(element);
//	}
//
//
//	/**
//	 * 将对象添加到缓存中.
//	* @param tel 手机号码.
//	 */
//	public boolean del(String tel) {
//		LOG.debug(tel+" delete cache");
//		 return cache.remove(tel);
//	}

	  //    <!-- 短信发送间隔设置——设置同一号码重复发送的时间间隔，一般设置为60-120秒 
    //    2、IP限定——根据自己的业务特点，设置单个IP针对一个手机号码每天的最大发送量
    //    3、手机号码限定——根据业务特点，设置每个手机号码每天的最大发送量
    //     -->
    //     <!-- 60 s gap  -->
    //    <cache name="phonegap" maxElementsInMemory="10000" eternal="false"
    //        timeToLiveSeconds="60" timeToIdleSeconds="60" overflowToDisk="true" />
    //    <!-- 24 h 10 times -->
    //    <cache name="phoneday"
    static Cache phonegapCache = cacheManager.getCache("phonegap");
    static Cache phonedayCache = cacheManager.getCache("phoneday");

    /** 获取getPhonegap中的code */
    public boolean getPhonegap(String tel) {
        if (phonegapCache != null) {
            phonegapCache.acquireReadLockOnKey(tel);
            try {
                Element e = phonegapCache.get(tel);
                if (e != null) {
//                	LOG.info("getPhonegap not new {},hits:{} create {} now {}", tel, e.getHitCount(), new Date(e.getCreationTime()),
//                			new Date(e.getCreationTime()));
                    return false;
                } else {
                    e = new Element(tel, new Date());
                    phonegapCache.put(e);
//                    LOG.info("getPhonegap  and new {},hits:{} create {} now {}", tel, e.getHitCount(), new Date(e.getCreationTime()),
//                    		new Date(e.getCreationTime()));
                    return true;
                }
            } finally {
                phonegapCache.releaseReadLockOnKey(tel);
            }
        }
        return false;
    }

    /** 获取getPhonegap中的code */
    public boolean getPhonedayCache(String tel) {
        if (phonedayCache != null) {
            phonedayCache.acquireReadLockOnKey(tel);
            try {
                Element e = phonedayCache.get(tel);
                LOG.info("phonedayCache  " + tel);
                if (e != null) {
//                	LOG.info("{} change ints {} create {} now {}", tel, e.getHitCount(), new Date(e.getCreationTime()), new Date(e.getCreationTime()));
                    if (e.getHitCount() > 9) {
                        return false;
                    }
                    return true;
                } else {
                    e = new Element(tel, 1);
                    phonedayCache.put(e);
                    return true;
                }
            } finally {
                phonedayCache.releaseReadLockOnKey(tel);
            }
        }
        return false;
    }
	

    /** 获取cache中的code */
    public String getCacheVerifyCode(String tel) {
        Element element = null;
        if (varifyCache != null) {
            // Read lock get data
            varifyCache.acquireReadLockOnKey(tel);
            try {
                element = varifyCache.get("verifycode_" + tel);
                if (element != null) {
                    String code = element.getObjectValue().toString();
                    LOG.info("getCacheCode get the cache : {},{}", tel, code);
                    return code;
                }
            } finally {
                varifyCache.releaseReadLockOnKey(tel);
            }
        }
        return null;
    }

    /** removeVerifyCode */
    public boolean removeVerifyCode(String tel) {
        if (varifyCache != null) {
            // Read lock get data
            varifyCache.acquireWriteLockOnKey(tel);
            try {
                varifyCache.remove("verifycode_" + tel);
                LOG.info("removeVerifyCode {}", tel);
                return true;
            } finally {
                varifyCache.releaseWriteLockOnKey(tel);
            }
        }
        return false;
    }

//    public static void main(String[] args) throws InterruptedException {
//        // test cache timeout
//        // <cache name="varify" maxElementsInMemory="10000" eternal="false"
//        // timeToLiveSeconds="6" timeToIdleSeconds="6" overflowToDisk="true" />
//        saveOrUpdateVerifyCode("123", "1234");
//        LOG.info("" + getCacheVerifyCode("123")); // 1234
//        Thread.sleep(1000 * 2);
//        LOG.info(getCacheVerifyCode("123")); // 1234
//        Thread.sleep(1000 * 5);
//        LOG.info(getCacheVerifyCode("123")); // null
//        Thread.sleep(1000 * 7);
//        LOG.info(getCacheVerifyCode("123")); // null
//        cacheManager.shutdown();
//    }

    /** saveOrUpdateVerifyCode */
    public String saveOrUpdateVerifyCode(String tel, String code) {
        if (varifyCache != null) {
            // Read lock get data
            varifyCache.acquireWriteLockOnKey(tel);
            try {
                Element element = new Element("verifycode_" + tel, code);
                varifyCache.put(element);
                LOG.info("saveOrUpdateVerifyCode {},{} ", tel, code);
            } finally {
                varifyCache.releaseWriteLockOnKey(tel);
            }
        }
        return null;
    }
    
}
