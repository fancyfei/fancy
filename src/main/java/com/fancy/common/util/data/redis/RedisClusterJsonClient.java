package com.fancy.common.util.data.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用的FastJson序列化工具，仅支持PoJo
 */
public class RedisClusterJsonClient {

	private final static Log log = LogFactory.getLog(RedisClusterJsonClient.class);
	
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
	public <T> T get(String key, Class<T> clazz) {
		if(!redisClusterClient.isEnable()) 
			return null;
		if(key == null) return null;
		if(clazz == null) return null;
		String obj = getJedisCluster().get(key);
		return JSON.parseObject(obj, clazz);
	}
	/**
	 * 缓存对象
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean set(String key, Object value) {
		if(!redisClusterClient.isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(value == null) : "value is null";
		String isOk = getJedisCluster().set(key, JSON.toJSONString(value));
		if(!"OK".equals(isOk)) {
			if(log.isInfoEnabled()) log.info("set error : " + isOk); 
			return false;
		}
		return true;
	}
	/**
	 * 缓存对象
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
		String isOk = getJedisCluster().setex(key, seconds, JSON.toJSONString(value));
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
		Map<String, String> hash = new HashMap<String, String>(value.size());
		if(value.size() > 0) {
			for(String ks : value.keySet()) {
				hash.put(ks, JSON.toJSONString(value.get(ks)));
			}
		}
		String isOk = getJedisCluster().hmset(key, hash);
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
	public <T> Map<String, T> getMap(String key, Class<T> clazz) {
		if(!redisClusterClient.isEnable()) 
			return null;
		if(key == null) return null;
		if(clazz == null) return null;
		Map<String, String> obj = getJedisCluster().hgetAll(key);
		Map<String, T> result = new HashMap<String, T>(obj.size());
		for(String ks : obj.keySet()) {
			result.put(ks, JSON.parseObject(obj.get(ks), clazz));
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
		getJedisCluster().hset(key, field, JSON.toJSONString(mValue));
		return true;
	}
	/**
	 * 获取Map对象中field对应的对象
	 * @param key
	 * @param field
	 * @param clazz
	 * @return
	 */
	public <T> T getToMap(String key, String field, Class<T> clazz) {
		if(!redisClusterClient.isEnable()) 
			return null;
		if(key == null) return null;
		if(field == null) return null;
		if(clazz == null) return null;
		String obj = getJedisCluster().hget(key, field);
		return JSON.parseObject(obj, clazz);
	}
	/**
	 * 获取数组对象
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getArray(String key, Class<T> clazz) {
		if(!redisClusterClient.isEnable()) 
			return null;
		if(key == null) return null;
		if(clazz == null) return null;
		String obj = getJedisCluster().get(key);
		return JSON.parseArray(obj, clazz);
	}
	public RedisClusterClient getRedisClusterClient() {
		return redisClusterClient;
	}
	public void setRedisClusterClient(RedisClusterClient redisClusterClient) {
		this.redisClusterClient = redisClusterClient;
	}

}
