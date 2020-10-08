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
import org.springframework.web.servlet.FlashMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @GetMapping({"/toIndex", "/", "/index", "/blog"})
    public String toIndex(Model model) {

        // 设置序列化编码
        redisTemplate.setKeySerializer(RedisUtils.setRedisSerializer());

        List<Blog> blogList = (List<Blog>) redisTemplate.opsForValue().get("blogList");

        if(null == blogList) {
            synchronized(this) {
                blogList = (List<Blog>) redisTemplate.opsForValue().get("blogList");
                if(null == blogList) {
                    blogList = blogService.queryAll();
                    // 在放入Redis的时候，直接给他设置了过期时间
                    // 四个参数的含义:                             key,value,过期时间,单位
                    redisTemplate.opsForValue().set("blogList", blogList, 300, TimeUnit.SECONDS);
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
    @GetMapping("/toTypes")
    public String toTypes(Model model, HttpServletRequest request) {

        model.addAttribute("blogSize", blogSize);
        model.addAttribute("totalView", totalView);
        model.addAttribute("messageSize", messageSize);
        List<Foreignkey> foreignkeyList = foreignkeyDao.queryAll();
        model.addAttribute("foreignkeyList", foreignkeyList);

        boolean b = true;

        String label = request.getParameter("label");

        if(label == null) { // 这个是第一次访问的时候啥标签都没选择的时候要显示的东西
            List<Blog> labelList = blogService.queryAll();
            int forNumber = 9;
            model.addAttribute("labelList", labelList);
            model.addAttribute("forNumber", forNumber);

        } else { // 走到这一步就是按照Lable查询
            List<Blog> labelList = blogService.queryByLabel(label);
            // 这种情况下，说明这个标签下面一篇文章都没有，则，直接不显示所有的即可
            if(labelList.size() == 0) {
                b = false;
                int forNumber = 0;
                model.addAttribute("labelList", labelList);
                model.addAttribute("forNumber", forNumber);
            } else {
                b = true; // 需要显示出东西来，则返回给前端参数
                int forNumber = labelList.size() - 1;
                model.addAttribute("labelList", labelList);
                model.addAttribute("forNumber", forNumber);
            }
        }
        model.addAttribute("b", b); // 用于校验是否要显示出来那些div

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
    @GetMapping("/toMessage")
    public String toMessage(Model model, HttpServletRequest request) {

        List<Message> messages = messageService.queryAll();
        int totalNumbers; // 记录总的页数
        if(messages.size() % 10 == 0) { // 直接/10就是总的页数
            totalNumbers = messages.size() / 10;
        } else {
            // 总的页数需要加1才是要显示的页数
            totalNumbers = messages.size() / 10 + 1;
        }
        model.addAttribute("totalNumbers", totalNumbers); // 总的页数


        final int pageSize = 10; // 规定只能一页10条数据

        int page = 1; // 先设定一个默认值为1，如果后面有东西的话，就替换掉，没有的话，就继续保持这个1

        // 这里优先选择上一页或者下一页的查询，表单输出查询优先级比较后
        if(request.getParameter("page") != null) { // 如果有参数的话，就给page赋值，当前的页数
            page = Integer.parseInt(request.getParameter("page"));
        } else if(request.getParameter("formPage") != null) { // 判断是否表单里面写了，如果写了的话也可以执行分页查询
            if(Integer.parseInt(request.getParameter("formPage")) >= totalNumbers) { // 进行判断，如果输入的数字太大，超过了原有的总的页数，就直接给他查询最后一夜的数据，不会报错，避免了异常
                page = totalNumbers;
            } else if(Integer.parseInt(request.getParameter("formPage")) < 1) { // 小于1，则显示的是第一页
                page = 1;
            } else {
                // 这些情况都不满足，则直接返回正确的页面
                page = Integer.parseInt(request.getParameter("formPage"));
            }
        }

        List<Message> messageList = messageService.queryByPage(page, pageSize);

        model.addAttribute("messageList", messageList);

        model.addAttribute("page", page); // 返回当前的页数

        // 判断上一页和下一页是否要显示出来
        boolean prePage = true;
        boolean nextPage = true;
        if(page == 1) { // 第一页，不需要显示出上一页
            prePage = false;
            nextPage = true;
        } else if(page == totalNumbers) {
            prePage = true;
            nextPage = false;
        }

        model.addAttribute("prePage", prePage);
        model.addAttribute("nextPage", nextPage); // 发送到前端作为前端是否要显示出来的判断

        // 页脚三兄弟
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("messageSize", messageSize);
        model.addAttribute("totalView", totalView);

        return "message";
    }

    // 时间轴
    @GetMapping("/toArchives")
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

        boolean b = true;
        List<Foreignkey> foreignkeyList = foreignkeyDao.queryAll();

        String question = request.getParameter("question");

        List<Blog> labelList = blogService.queryByLabel(question);

        int forNumber = labelList.size() - 1;
        model.addAttribute("labelList", labelList);
        model.addAttribute("forNumber", forNumber);

        model.addAttribute("b", b);
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

        model.addAttribute("url", "http://101.200.78.148:8080/QRCodes/" + blog.getId() + ".jpg");

        model.addAttribute("blog", blog);

        model.addAttribute("blogSize", blogSize);

        model.addAttribute("messageSize", messageSize);

        model.addAttribute("totalView", totalView);

        return "blog";

    }

































}
