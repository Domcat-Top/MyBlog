package com.tom;

import com.tom.dao.BlogDao;
import com.tom.pojo.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyblogApplicationTests {


    @Autowired
    BlogDao blogDao;

    @Test
    void contextLoads() {
    }

    @Test
    void test01() {
        int i = blogDao.addView(1);

    }

}
