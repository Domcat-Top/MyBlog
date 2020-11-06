package com.tom.utils.aliUtils;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: Tom
 * @create: 2020-10-17 20:59
 * @description: 读取配置文件参数
 **/
@Data
@Component
public class ConstantVodUtils implements InitializingBean {


    // 通过Spring初始化的时候，就把配置文件里面的这两个参数读取出来，然后赋值给工具类的这两个属性
    // 实现工具类的效果
    @Value("${aliyun.vod.file.keyId}")
    private String keyid;

    @Value("${aliyun.vod.file.keysecret}")
    private String keysecret;

    public static String ACCESS_KEY_SECRET;
    public static String ACCESS_KEY_ID;


    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = keyid;
        ACCESS_KEY_SECRET = keysecret;
    }
}
