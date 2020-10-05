package com.tom.controller;

import com.tom.dao.BlogDao;
import com.tom.dao.MessageDao;
import com.tom.pojo.Blog;
import com.tom.pojo.Message;
import com.tom.service.BlogService;
import com.tom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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









}
