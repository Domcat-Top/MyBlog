package com.tom.utils.aliUtils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: Tom
 * @create: 2020-10-14 13:49
 * @description: 用于读取配置文件的内容
 **/

// 解释：实现这个接口，Spring初始化的时候，执行这个接口的方法
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    // 读取配置文件内容
    @Value("${aliyun.oss.file.endpoint}") // 属性注入---直接给属性赋值
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    // 定义公开静态常量
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
