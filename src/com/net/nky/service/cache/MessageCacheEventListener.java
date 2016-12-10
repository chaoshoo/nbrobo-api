package com.net.nky.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

/**
 * 缓存事件.
 * @author Ken
 * @version 2016年8月19日
 */
public class MessageCacheEventListener implements CacheEventListener {

//	public static final MessageCacheEventListener INSTANCE = new MessageCacheEventListener();

	private static final Logger LOG = LoggerFactory.getLogger(MessageCacheEventListener.class);

	@Override
	public void notifyElementRemoved(final Ehcache cache, final Element element) throws CacheException {
		LOG.debug(element.getObjectKey() + " notifyElementRemoved " + element.getObjectValue());
	}

	@Override
	public void notifyElementPut(final Ehcache cache, final Element element) throws CacheException {
		LOG.debug(element.getObjectKey() + " notifyElementPut " + element.getObjectValue());
		removeIfNull(cache, element);
	}

	@Override
	public void notifyElementUpdated(final Ehcache cache, final Element element) throws CacheException {
		LOG.debug(element.getObjectKey() + " notifyElementUpdated " + element.getObjectValue());
		removeIfNull(cache, element);
	}

	private void removeIfNull(final Ehcache cache, final Element element) {
		LOG.debug(element.getObjectKey() + " removeIfNull " + element.getObjectValue());
		if (element.getObjectValue() == null) {
			cache.remove(element.getObjectKey());
		}
	}

	@Override
	public void notifyElementExpired(final Ehcache cache, final Element element) {
		LOG.debug(element.getObjectKey() + " notifyElementExpired " + element.getObjectValue());
	}

	@Override
	public void notifyElementEvicted(final Ehcache cache, final Element element) {
		LOG.debug(element.getObjectKey() + " notifyElementEvicted " + element.getObjectValue());
	}

	@Override
	public void notifyRemoveAll(final Ehcache cache) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Singleton instance");
	}
}