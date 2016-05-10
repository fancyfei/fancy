package com.fancy.common.util.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RedisClusterClient {
	
	private final int DEFULT_PORT = 6379;
	
	private final static Log log = LogFactory.getLog(RedisClusterClient.class);
	
	private Set<HostAndPort> jedisClusterNodes;
	private JedisPoolConfig poolConfig;
	private JedisCluster jedisCluster;
	private boolean enable = true; 
	private int timeout = 2000;
	private int maxRedirections = 5;
	/**
	 * 初始化工具
	 * 
	 * @param redisServer 逗号分隔的IP:port列表
	 * @param timeout 超时
	 * @param maxRedirections 最大
	 * @param poolConfig
	 */
	public RedisClusterClient(String redisServer, final JedisPoolConfig poolConfig, int timeout, int maxRedirections) {
		log.info("init Cluster ip:"+redisServer);
		this.poolConfig = poolConfig;
		this.timeout = timeout;
		this.maxRedirections = maxRedirections;
		jedisClusterNodes = getNodes(redisServer);
	}
	public RedisClusterClient(String redisServer, final JedisPoolConfig poolConfig) {
		log.info("init Cluster ip:"+redisServer);
		jedisClusterNodes = getNodes(redisServer);
		this.poolConfig = poolConfig;
	}
	/**
	 * 默认最大30个连接
	 * @param redisServer
	 */
	public RedisClusterClient(String redisServer) {
		log.info("init Cluster ip:"+redisServer);
		poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(30);
		poolConfig.setMaxIdle(15);
		poolConfig.setMinIdle(10);
		poolConfig.setMaxWaitMillis(1000);
		
		poolConfig.setBlockWhenExhausted(true);
		jedisClusterNodes = getNodes(redisServer);
	}

	private Set<HostAndPort> getNodes(String redisServer) {
		String[] servers = redisServer.replaceAll(" ", "").split(",");
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		for (String ipPort : servers) {
			String host = null;
			int port = DEFULT_PORT;
			if(ipPort.contains(":")) {
				host = ipPort.split(":")[0];
				port = Integer.parseInt(ipPort.split(":")[1]);
			}else{
				host = ipPort;
			}
			jedisClusterNodes.add(new HostAndPort(host, port));
		}
		return jedisClusterNodes;
	}
	
	private synchronized void init() {
		jedisCluster = new JedisCluster(jedisClusterNodes, timeout, maxRedirections, poolConfig);
		if(jedisCluster.getClusterNodes() == null || jedisCluster.getClusterNodes().size() <= 0){
			log.error("Redis Cluster not nodes");
			enable = false;
		} else {
			log.info("Cluster nodes:"+jedisCluster.getClusterNodes().keySet());
		}
	}
	/**
	 * 删除对象
	 * @param key
	 * @return
	 */
	public boolean remove(String key) {
		if(!isEnable()) 
			return false;
		if(key == null) return false;
		this.getJedisCluster().del(key);
		return true;
	}
	/**
	 * 缓存字符串对象
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean set(String key, String value, int seconds) {
		if(!isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(value == null) : "value is null";
		assert(seconds <= 0) : "seconds less than 0";
		String isOk = this.getJedisCluster().setex(key, seconds, value);
		if(!"OK".equals(isOk)) {
			if(log.isInfoEnabled()) log.info("set error : " + isOk); 
			return false;
		}
		return true;
	}
	/**
	 * 获取字符串对象
	 * @param key
	 * @return
	 */
	public String get(String key) {
		if(!isEnable()) 
			return null;
		if(key == null) return null;
		return this.getJedisCluster().get(key);
	}
	/**
	 * 存放Map对象
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setMap(String key, Map<String, String> value) {
		if(!isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(value == null) : "value is null";
		String isOk = this.getJedisCluster().hmset(key, value);
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
	public Map<String, String> getMap(String key) {
		if(!isEnable()) 
			return null;
		if(key == null) return null;
		return this.getJedisCluster().hgetAll(key);
	}
	/**
	 * 存放字符串到Map类型的值中去
	 * @param key
	 * @param field
	 * @param mValue
	 * @return
	 */
	public boolean setMapField(String key, String field, String mValue) {
		if(!isEnable()) 
			return false;
		assert(key == null) : "key is null";
		assert(field == null) : "field is null";
		assert(mValue == null) : "mValue is null";
		this.getJedisCluster().hset(key, field, mValue);
		return true;
	}
	/**
	 * 获取Map对象中field对应的字符串
	 * @param key
	 * @param field
	 * @param clazz
	 * @return
	 */
	public String getMapField(String key, String field) {
		if(!isEnable()) 
			return null;
		if(key == null) return null;
		if(field == null) return null;
		return this.getJedisCluster().hget(key, field);
	}
	
	public JedisCluster getJedisCluster() {
		if(!isEnable()) 
			return null;
		if(jedisCluster == null) {
			init();
		}
		return jedisCluster;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
