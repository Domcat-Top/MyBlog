package com.tom;

import com.tom.dao.BlogDao;
import com.tom.dao.MessageDao;
import com.tom.pojo.Blog;
import com.tom.pojo.Message;
import com.tom.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


class MyblogApplicationTests {

    @Autowired
    BlogService blogService;


    @Autowired
    BlogDao blogDao;

    @Autowired
    MessageDao m,messageDao;

    @Test
    void contextLoads() {
    }

    @Test
    void test01() {
        int i = blogDao.addView(1);

    }

    // 测试分页查询是否可用
    @Test
    void test02() {

        List<Message> messages = messageDao.queryByPage(1, 10);

        System.out.println(messages.toString());

    }

    // 测试首页的查询是否可以生效
    @Test
    void test03() {
        List<Blog> blogList = blogService.queryByPageAndPageSize(1, 10);
        for(Blog blog : blogList) {
            System.out.println(blog.toString());
        }
    }



}
