package com.tom.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *
 * redis的专用工具类
 */
public class RedisUtils {


    // 设置redis放入的key的序列化
    public static RedisSerializer setRedisSerializer() {
        return new StringRedisSerializer();
    }




}
