package com.fancy.common.util.data.redis;

import com.fancy.common.util.comm.BeanHessionSerializeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用的序列化工具
 * <br/>序列化后（耗时仅FastJson的1/10），且体积小。
 */
public class RedisClusterPojoClient {

	private final static Log log = LogFactory.getLog(RedisClusterPojoClient.class);
	
	private RedisClusterClient redisClusterClient;

	public JedisCluster getJedisCluster() {
		return redisClusterClient.getJedisCluster();
	}
	/**
	 * 删除对象
	 * @param key
	 * @return
	 */
	public boolean remove(String key){
		return redisClusterClient.remove(key);
	}
	/**
	 * 获取对象
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> clazz) {
		if(!redisClusterClient.isEnable()) 
			return null;
		if(key == null) return null;
		if(clazz == null) return null;
		byte[] obj = getJedisCluster().get(key.getBytes());
		return (T) BeanHessionSerializeUtil.deserialize(obj);
	}

	/**
	 * 缓存Object对象
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean set(String key, Object value, int seconds) {
		if(!redisClusterClient.isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(value == null) : "value is null";
		assert(seconds <= 0) : "seconds less than 0";
		String isOk = getJedisCluster().setex(key.getBytes(), seconds, BeanHessionSerializeUtil.serialize(value));
		if(!"OK".equals(isOk)) {
			if(log.isInfoEnabled()) log.info("set error : " + isOk); 
			return false;
		}
		return true;
	}
	/**
	 * 缓存Object对象
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean set(String key, Object[] value, int seconds) {
		if(!redisClusterClient.isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(value == null) : "value is null";
		assert(seconds <= 0) : "seconds less than 0";
		String isOk = getJedisCluster().setex(key.getBytes(), seconds, BeanHessionSerializeUtil.serialize(value));
		if(!"OK".equals(isOk)) {
			if(log.isInfoEnabled()) log.info("set error : " + isOk); 
			return false;
		}
		return true;
	}
	/**
	 * 存放Map对象
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setMap(String key, Map<String, Object> value) {
		if(!redisClusterClient.isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(value == null) : "value is null";
		Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>(value.size());
		if(value.size() > 0) {
			for(String ks : value.keySet()) {
				hash.put(ks.getBytes(), BeanHessionSerializeUtil.serialize(value.get(ks)));
			}
		}
		String isOk = getJedisCluster().hmset(key.getBytes(), hash);
		if(!"OK".equals(isOk)) {
			if(log.isInfoEnabled()) log.info("set error : " + isOk); 
			return false;
		}
		return true;
	}
	
	/**
	 * 获取Map对象
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getMap(String key, Class<T> clazz) {
		if(!redisClusterClient.isEnable()) 
			return null;
		if(key == null) return null;
		if(clazz == null) return null;
		Map<byte[], byte[]> obj = getJedisCluster().hgetAll(key.getBytes());
		Map<String, T> result = new HashMap<String, T>(obj.size());
		for(byte[] ks : obj.keySet()) {
			result.put(new String(ks), (T)BeanHessionSerializeUtil.deserialize(obj.get(ks)));
		}
		return result;
	}
	/**
	 * 存放内容到Map类型的值中去
	 * @param key
	 * @param field
	 * @param mValue
	 * @return
	 */
	public boolean setToMap(String key, String field, Object mValue) {
		if(!redisClusterClient.isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(field == null) : "field is null";
		assert(mValue == null) : "mValue is null";
		getJedisCluster().hset(key.getBytes(), field.getBytes(), BeanHessionSerializeUtil.serialize(mValue));
		return true;
	}
	/**
	 * 获取Map对象中field对应的对象
	 * @param key
	 * @param field
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getToMap(String key, String field, Class<T> clazz) {
		if(!redisClusterClient.isEnable()) 
			return null;
		if(key == null) return null;
		if(field == null) return null;
		if(clazz == null) return null;
		byte[] obj = getJedisCluster().hget(key.getBytes(), field.getBytes());
		return (T)BeanHessionSerializeUtil.deserialize(obj);
	}
	public RedisClusterClient getRedisClusterClient() {
		return redisClusterClient;
	}
	public void setRedisClusterClient(RedisClusterClient redisClusterClient) {
		this.redisClusterClient = redisClusterClient;
	}

}
