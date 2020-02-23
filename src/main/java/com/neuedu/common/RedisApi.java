package com.neuedu.common;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 封装redis中字符串、哈希、列表、集合、有序集合api
 * */
@Component
public class RedisApi {
    @Resource
    JedisPool jedisPool;


    //字符串结构
    /**
     * 字符串
     * 添加key，value
     * */
    public String set(String key, String value){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }
    /**
     * 字符串
     * 根据key获取value
     * */
    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    /**
     *字符串
     * key 存在，设置不成功
     * key不存在，设置成功
     */
    public Long setNx(String key,String value){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.setnx(key, value);
        jedis.close();
        return result;
    }

    /**
     * 字符串
     * 保证原则性
     * 先get再set
     * */
    public String getSet(String key,String value){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.getSet(key, value);
        jedis.close();
        return result;
    }

    /**
     * 字符串
     *为key设置过期时间
     * */
    public Long expire(String key,Integer timeout){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.expire(key, timeout);
        jedis.close();
        return result;
    }

    /**
     * 字符串
     * 查看key的剩余时间
     * */
    public Long ttl(String key){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.ttl(key);
        jedis.close();
        return result;
    }

    /**
     * 字符串
     * 在设置key，value时，为key指定过期时间
     * */
    public String setx(String key,Integer timeout,String value){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.setex(key,timeout,value);
        jedis.close();
        return result;
    }

    /**
     * 删除一个key
     * */
    public Long del(String key){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }


    //哈希结构-api

    /**
     * 设置key，field，value
     * */
    public Long hset(String key,String field,String value){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(key,field,value);
        jedis.close();
        return result;
    }

    /**
     * 批量设置key，field，value
     * 例如:user:1:info  name  moxiaoyu
     * */
    public Long hset(String key, Map<String,String> hash){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(key,hash);
        jedis.close();
        return result;
    }

    /**
     * 根据key，field查看value
     * */
    public String hget(String key,String feild){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(key,feild);
        jedis.close();
        return result;
    }

    /**
     * 根据key，field查看所有value
     * */
    public Map<String,String> hgetAll(String key){
        Jedis jedis = jedisPool.getResource();
        Map result = jedis.hgetAll(key);
        jedis.close();
        return result;
    }

    /**
     * 根据key查看所有的feild
     * */
    public Set<String> hkeys(String key){
        Jedis jedis = jedisPool.getResource();
        Set result = jedis.hkeys(key);
        jedis.close();
        return result;
    }

    /**
     * 根据key查看所有的value
     * */
    public List<String> hvals(String key){
        Jedis jedis = jedisPool.getResource();
        List result = jedis.hvals(key);
        jedis.close();
        return result;
    }

    /**
     * 计数器
     * */
    public Long hincrBy(String key,String field,Long value){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hincrBy(key, field,value );
        jedis.close();
        return result;
    }

}
