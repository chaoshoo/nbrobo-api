package com.net.nky.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Service;

@Service(value="redisService")
public class RedisService {

	private Logger LOG = LoggerFactory.getLogger(RedisService.class);

	@Autowired
	private List<RedisTemplate<String, String>> redisTemplateList;

	/**
	 * 从缓存中删除指定的key
	 * @param keys
	 */
	public void del(final String... keys) {
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				redisTemplate.execute(new RedisCallback<Long>() {
					public Long doInRedis(RedisConnection connection) throws DataAccessException {
						long result = 0;
						for (int i = 0; i < keys.length; i++) {
							result = connection.del(keys[i].getBytes());
						}
						return result;
					}
				});

			} catch (Exception e) {
				LOG.error("删除异常", e);
			}
		}
	}

	/**
	 * 重缓存中删除指定的key 模式匹配，效率低
	 * @param keys
	 */
	public void delByReg(final String... keys) {
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				redisTemplate.execute(new RedisCallback<Long>() {
					public Long doInRedis(RedisConnection connection) throws DataAccessException {
						long result = 0;
						for (int i = 0; i < keys.length; i++) {
							Set<byte[]> keyset = connection.keys((keys[i] + "*").getBytes());
							for (byte[] key : keyset) {
								result = connection.del(key);
							}
						}
						return result;
					}
				});

			} catch (Exception e) {
				LOG.error("delByReg异常", e);
			}
		}
	}

	/**
	 * 判断一个键是否存在于缓存中
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		Boolean result = false;
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				result = (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
					public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
						return connection.exists(key.getBytes());
					}
				});

				if (result) {
					break;
				}
			} catch (Exception e) {
				LOG.error("exists异常", e);
			}
		}

		return result;
	}

	/**
	 * 向缓存中插入数据
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public void set(final byte[] key, final byte[] value, final long liveTime) {
		if (value == null)
			return;
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				redisTemplate.execute(new RedisCallback<Object>() {
					public Long doInRedis(final RedisConnection connection) throws DataAccessException {
						connection.set(key, value);
						if (liveTime > 0) {
							connection.expire(key, liveTime);
						}
						return 1L;
					}
				});
			} catch (Exception e) {
				LOG.error("向缓存中插入数据异常", e);
			}
		}
	}

	public <T> void set(final byte[] key, final Object value, final long liveTime, Class<T> type) {
		final JacksonJsonRedisSerializer<T> serializer = new JacksonJsonRedisSerializer<T>(type);
		byte[] bv = serializer.serialize(value);
		this.set(key, bv, liveTime);
	}

	public void set(String key, String value, long liveTime) {
		if (value == null)
			return;
		this.set(key.getBytes(), value.getBytes(), liveTime);
	}

	/**
	 * 从缓存中获取数据
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		String cacheValue = "";
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				cacheValue = redisTemplate.execute(new RedisCallback<String>() {
					public String doInRedis(RedisConnection connection) throws DataAccessException {
						try {
							byte[] cacheBytes = connection.get(key.getBytes());
							if (cacheBytes != null) {
								String cacheStr = new String(cacheBytes, "utf-8");
								return cacheStr;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						return "";
					}
				});
				if (cacheValue != null && !"".equals(cacheValue))
					break;
			} catch (Exception e) {
				LOG.error("从缓存中获取数据异常", e);
			}
		}
		return cacheValue;
	}

	public <T> T get(final String key, final Class<T> clazz) {
		T t = null;
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			t = redisTemplate.execute(new RedisCallback<T>() {
				public T doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] data = connection.get(key.getBytes());
					if (data != null)
						return new JacksonJsonRedisSerializer<T>(clazz).deserialize(data);
					return null;
				}
			});

			if (t != null)
				break;
		}
		return t;
	}

	/**
	 * 清空缓存
	 */
	public void flushDB() {
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				redisTemplate.execute(new RedisCallback<String>() {
					public String doInRedis(RedisConnection connection) throws DataAccessException {
						connection.flushDb();
						return "ok";
					}
				});
			} catch (Exception e) {
				LOG.error("清空缓存异常", e);
			}
		}
	}

	/**
	 * 添加至有序集合
	 * @param key
	 * @param score
	 * @param value
	 */
	public void zadd(final byte[] key, final double score, final byte[] value) {
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				redisTemplate.execute(new RedisCallback<String>() {
					public String doInRedis(RedisConnection connection) throws DataAccessException {
						connection.zAdd(key, score, value);
						return "ok";
					}
				});
			} catch (Exception e) {
				LOG.error("添加至有序集合异常", e);
			}
		}
	}

	/**
	 * 按条件获取有序集合元素子集
	 * @param key
	 *            有序集合key
	 * @param min
	 *            范围最小值
	 * @param max
	 *            范围最大值
	 * @param offset
	 *            从第0ffset+1个元素起
	 * @param count
	 *            返回上限
	 * @return
	 */
	public Set<byte[]> zRangeByScore(final byte[] key, final double min, final double max, final long offset,
			final long count) {
		Set<byte[]> set = null;
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				set = redisTemplate.execute(new RedisCallback<Set<byte[]>>() {

					public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
						return connection.zRangeByScore(key, min, max, offset, count);
					}

				});
				if (set != null) {
					break;
				}
			} catch (Exception e) {
				LOG.error(" 按条件获取有序集合元素子集异常", e);
			}
		}
		return set;
	}

	/**
	 * 添加指定map至缓存
	 * @param key map唯一标识
	 * @param hashes
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void hMSet(final byte[] key, final Map<byte[], byte[]> hashes) {
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				redisTemplate.execute(new RedisCallback() {
					public Object doInRedis(RedisConnection connection) throws DataAccessException {
						connection.hMSet(key, hashes);
						return null;
					}
				});

			} catch (Exception e) {
				LOG.error(" 添加指定map至缓存异常", e);
			}
		}
	}

	/**
	 * 获取指定map中指定键对应的值列表
	 * @param key map的唯一标识
	 * @param fields 键数组
	 * @return 值数组
	 */
	public List<byte[]> hMGet(final byte[] key, final byte[]... fields) {
		List<byte[]> cacheValue = null;
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				cacheValue = redisTemplate.execute(new RedisCallback<List<byte[]>>() {
					public List<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
						return connection.hMGet(key, fields);
					}
				});
				if (cacheValue != null) {
					break;
				}
			} catch (Exception e) {
				LOG.error("获取指定map中指定键对应的值列表异常", e);
			}
		}
		return cacheValue;
	}

	/**
	 * 添加Object到缓存
	 * 
	 */
	public void setObj(final byte[] key, final Object obj) {
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				redisTemplate.execute(new RedisCallback<Object>() {
					public Object doInRedis(RedisConnection connection) throws DataAccessException {
						JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
						byte[] bobj = serializer.serialize(obj);
						connection.set(key, bobj);
						return null;
					}
				});
			} catch (Exception e) {
				LOG.error("添加Object到缓存异常", e);
			}
		}
	}

	/**
	 * 取Object
	 * @param key
	 */
	public Object getObj(final byte[] key) {
		Object obj = null;
		for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
			try {
				obj = redisTemplate.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection connection) throws DataAccessException {
						byte[] data = connection.get(key);
						return new JdkSerializationRedisSerializer().deserialize(data);
					}

				});
			} catch (Exception e) {
				LOG.error("取Object异常", e);
				obj = "error";
			}
		}
		return obj;
	}

	// getters and setters
	public List<RedisTemplate<String, String>> getRedisTemplateList() {
		return redisTemplateList;
	}

	public void setRedisTemplateList(List<RedisTemplate<String, String>> redisTemplateList) {
		this.redisTemplateList = redisTemplateList;
	}

}
