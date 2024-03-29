package com.tom.controller;

import com.tom.dao.BlogDao;
import com.tom.dao.MessageDao;
import com.tom.pojo.Blog;
import com.tom.pojo.Foreignkey;
import com.tom.pojo.Message;
import com.tom.service.BlogService;
import com.tom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestController {


    @Autowired
    BlogService blogService;

    @Autowired
    MessageService messageService;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        List<Blog> blogList = blogService.queryAll();
        return blogList.toString();
    }

    @RequestMapping("/test02/{pageNumber}")
    public String test02(@PathVariable("pageNumber") int pageNumber) {
        List<Blog> blogList = blogService.queryByPage(pageNumber - 1);
        return blogList.toString();
    }

    @RequestMapping("/test03")
    @ResponseBody
    public String test03() {
        List<Blog> blogList = blogService.queryAll();

        String s = blogList.get(0).formatCreateTime();

        System.out.println(s);
        System.out.println(s);
        return s;
    }

    // 去404
    @RequestMapping("/404")
    public String to404() {
        return "error/404";
    }

    // 去500
    @RequestMapping("/500")
    public String to500() {
        return "error/500";
    }

    @RequestMapping("/testPage")
    @ResponseBody
    public String toPage() {

        List<Message> messages = messageService.queryByPage(1, 10);

        return messages.toString();
    }

    @ResponseBody
    @RequestMapping("/testGithub")
    public String toGithub() {

        return "redirect:";
    }

    // 测试首页的分页是否成功
    @ResponseBody
    @RequestMapping("/testIndexPage")
    public String testIndexPage() {
        List<Blog> blogList = blogService.queryByPageAndPageSize(1, 10);
        return blogList.toString();
    }

    // 测试去到Video页面
    @RequestMapping("/video")
    public String video() {
        return "video";
    }


    // 测试一下上传是否可以
    @RequestMapping("/toTest")
    public String toTest() {
        return "test";
    }

    // 测试一下查询使用最多的标签数量
    @RequestMapping("/maxLabel")
    @ResponseBody
    public String maxLabel() {
        Foreignkey foreignkey = blogService.queryMaxLabelBlog();
        return foreignkey.getLabel();
    }

    // 测试一下倒序查询出使用最多的标签类型
    @RequestMapping("/descLabelStyle")
    @ResponseBody
    public List<Foreignkey> descLabelStyle() {
        return blogService.queryByAppearTimesOfLabel();
    }





}
