package com.tom;

import com.tom.dao.BlogDao;
import com.tom.dao.MessageDao;
import com.tom.pojo.Blog;
import com.tom.pojo.Foreignkey;
import com.tom.pojo.Message;
import com.tom.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


class MyblogApplicationTests {

    @Autowired
    BlogService blogService;
    @Autowired
    BlogDao blogDao;

    @Test
    void contextLoads() {
    }

}
