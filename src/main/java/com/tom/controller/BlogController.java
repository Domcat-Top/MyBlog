package com.tom.controller;

import com.tom.dao.BlogDao;
import com.tom.dao.ForeignkeyDao;
import com.tom.pojo.Blog;
import com.tom.pojo.Foreignkey;
import com.tom.pojo.Message;
import com.tom.service.BlogService;
import com.tom.service.MessageService;
import com.tom.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class BlogController {

    // 博客总数
    private static int blogSize;
    // 留言总数
    private static int messageSize;
    // 访问总数
    private static int totalView;

    @Autowired
    BlogService blogService;
    @Autowired
    ForeignkeyDao foreignkeyDao;
    @Autowired
    MessageService messageService;
    @Autowired
    RedisTemplate redisTemplate;

    // 首页
    @RequestMapping({"/toIndex", "/", "index", "/blog"})
    public String toIndex(Model model) {

        // 设置序列化编码
        redisTemplate.setKeySerializer(RedisUtils.setRedisSerializer());

        List<Blog> blogList = (List<Blog>) redisTemplate.opsForValue().get("blogList");

        if(null == blogList) {
            synchronized(this) {
                blogList = (List<Blog>) redisTemplate.opsForValue().get("blogList");
                if(null == blogList) {
                    blogList = blogService.queryAll();
                    redisTemplate.opsForValue().set("blogList", blogList);
                }
            }
        }

        BlogController.messageSize = messageService.queryAll().size();
        BlogController.totalView = blogService.getTotalView();

        int length = 10;

        blogSize = blogList.size();

        model.addAttribute("blogList", blogList);

        model.addAttribute("length", length - 1);

        model.addAttribute("blogSize", blogSize);

        model.addAttribute("messageSize", BlogController.messageSize);

        model.addAttribute("totalView", totalView);

        return "index";
    }

    // 分类
    @RequestMapping("/toTypes")
    public String toTypes(Model model, HttpServletRequest request) {

        model.addAttribute("blogSize", blogSize);
        model.addAttribute("totalView", totalView);
        model.addAttribute("messageSize", messageSize);

        List<Foreignkey> foreignkeyList = foreignkeyDao.queryAll();

        String label = request.getParameter("label");

        if(label == null) {
            List<Blog> labelList = blogService.queryAll();
            int forNumber = 9;
            model.addAttribute("labelList", labelList);
            model.addAttribute("forNumber", forNumber);
        } else {
            List<Blog> labelList = blogService.queryByLabel(label);
            int forNumber = labelList.size() - 1;
            model.addAttribute("labelList", labelList);
            model.addAttribute("forNumber", forNumber);
        }

        model.addAttribute("foreignkeyList", foreignkeyList);

        return "types";
    }

    // 关于我
    @RequestMapping("/toAbout")
    public String toAbout(Model model) {
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("messageSize", messageSize);
        model.addAttribute("totalView", totalView);

        return "about";
    }

    // 留言板
    @RequestMapping("/toMessage")
    public String toMessage(Model model) {
        model.addAttribute("blogSize", blogSize);

        List<Message> messageList = messageService.queryAll();

        model.addAttribute("messageList", messageList);

        model.addAttribute("messageSize", messageSize);

        model.addAttribute("totalView", totalView);

        return "message";
    }

    // 时间轴
    @RequestMapping("/toArchives")
    public String toArchives(Model model) {

        List<Blog> blogList = blogService.queryAll();

        model.addAttribute("blogList", blogList);
        // 获取到总的博客数量
        model.addAttribute("blogSize", blogSize);
        // 设定循环的次数
        model.addAttribute("forNumber", blogSize-1);

        model.addAttribute("messageSize", messageSize);

        model.addAttribute("totalView", totalView);

        return "archives";
    }

    // 导航栏搜索
    @GetMapping("/toSearch")
    public String toSearch(Model model, HttpServletRequest request) {

        String question = request.getParameter("question");

        List<Blog> blogList = blogService.queryByLike(question);

        model.addAttribute("blogSize", blogSize);

        if(blogList.size() == 0 || question == null) {
            return "error/searchNothing";
        }
        model.addAttribute("blogList", blogList);

        model.addAttribute("blogSize", blogSize);

        model.addAttribute("messageSize", messageSize);

        model.addAttribute("totalView", totalView);

        return "search";

    }

    // 博客右下角标签的搜索
    @GetMapping("/toLabelTypes")
    public String toLabelSearch(Model model, HttpServletRequest request) {

        List<Foreignkey> foreignkeyList = foreignkeyDao.queryAll();

        String label = request.getParameter("question");

        List<Blog> labelList = blogService.queryByLabel(label);
        int forNumber = labelList.size() - 1;
        model.addAttribute("labelList", labelList);
        model.addAttribute("forNumber", forNumber);

        model.addAttribute("foreignkeyList", foreignkeyList);

        model.addAttribute("blogSize", blogSize);

        model.addAttribute("messageSize", messageSize);

        model.addAttribute("totalView", totalView);

        return "types";

    }

    // 去往404页面
    @RequestMapping("/error/404")
    public String toLose(Model model) {
        model.addAttribute("blogSize", blogSize);

        model.addAttribute("messageSize", messageSize);

        model.addAttribute("totalView", totalView);

        return "error/404";
    }

    // 博客详情
    @GetMapping("/toBlog")
    public String toBlog(Model model, HttpServletRequest request) {

        String id = request.getParameter("id");

        // 修改博客的View
        blogService.addView(Integer.parseInt(id));

        // 查询获得具体的博客
        Blog blog = blogService.queryByID(Integer.parseInt(id));

        // 解码，获取到读取的博客内容
        String blogContent = com.tom.utils.MarkdownUtils.markdownToHtmlExtensitons(blog.getBlogContent());

        // 重新放入并且输出
        blog.setBlogContent(blogContent);

        model.addAttribute("blog", blog);

        model.addAttribute("blogSize", blogSize);

        model.addAttribute("messageSize", messageSize);

        model.addAttribute("totalView", totalView);

        return "blog";

    }

































}
