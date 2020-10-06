package com.tom.config;

/*
*
* Redis的配置类
 */

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching // 开启缓存
public class RedisConfig {


    /**
     * 这里这个方法没有用到，直接在Controller实现的时候加了过期时间，所以这里暂时先注释掉
     */
    // public CacheManager cacheManager(RedisTemplate redisTemplate) {
    //
    //     RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
    //
    //     // 设置缓存过期时间
    //     Map<String, Long> map = new HashMap<>();
    //     map.put("12h", 3600 * 12L);
    //     map.put("1h", 3600 * 1L);
    //     map.put("10m", 60 * 10L);
    //     redisCacheManager.setExpires(map);
    //
    //     return redisCacheManager;
    //
    // }


}
