package com.tom.controller;

import com.aliyuncs.exceptions.ClientException;
import com.tom.dao.BlogDao;
import com.tom.dao.ForeignkeyDao;
import com.tom.pojo.Blog;
import com.tom.pojo.Foreignkey;
import com.tom.pojo.Message;
import com.tom.pojo.vo.DescVideo;
import com.tom.pojo.vo.PlayVideo;
import com.tom.service.BlogService;
import com.tom.service.MessageService;
import com.tom.service.VideoService;
import com.tom.utils.RedisUtils;
import com.tom.utils.aliUtils.MyAliyunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.FlashMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@CrossOrigin
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
    VideoService videoService;

    @Autowired
    RedisTemplate redisTemplate;


    // 首页
    @GetMapping({"/toIndex", "/", "/index", "/blog"})
    public String toIndex(Model model, HttpServletRequest request) {

        // 给新加的模块查询数据
        List<DescVideo> descVideoList = videoService.queryIndexVideo();
        if(descVideoList.size() == 0) {
            model.addAttribute("videoBoolean", false);
        } else {
            model.addAttribute("videoBoolean", true);
        }

        // 把得到的数据传递给前端页面，显示出来
        model.addAttribute("descVideoList", descVideoList);
        model.addAttribute("videoSize", descVideoList.size() - 1); // 后端直接处理好数据，以防万一发送到前端还需要继续进行处理

        // 设置一个初始页，也就是第一次访问要展示的，第一页
        int page = 1;
        // 进行分页查询，查询出当前page对应的所有东西，根据数据库的东西来定
        // 也就不需要自己手动确定页面显示数据的长度
        // 先确定查询的时候一页多少
        final int pageSize = 10; // 一页十条查询

        // 判断是否是以后的访问，如果是之后的访问，那么就会有数据从前端传递，也就是说，page是有值的
        if(request.getParameter("page") != null) { // 前段传递过值来了
            page = Integer.parseInt(request.getParameter("page")); // 给page赋值前端的值
        }

        int totalPage; // 用于记录总的页数
        List<Blog> queryAll = blogService.queryAll();

        blogSize = queryAll.size(); // 这个就是从始至终，记录整个博客数量的变量，会一直用到
        messageSize = messageService.queryAll().size();
        totalView = blogService.getTotalView();

        // 这里有个误区，就是如果记录数小于十，也是会出问题的，但是由于我们这里的数据超过了十条，也就不考虑那个问题了
        if(queryAll.size() % 10 == 0) { // 可以整除，页数不需要+1
            totalPage = queryAll.size() / 10;
        } else {
            totalPage = queryAll.size() / 10 + 1;
        }
        // 用于判断是否显示上一页和下一页---除了第一页和最后一页的特殊情况下，显示一个，其他情况下的，都是都显示出来的，所以默认都给他为true
        boolean prePage = true;
        boolean nextPage = true;

        if(page == 1) { // 第一页没有上一页，则不显示
            prePage = false;
            nextPage = true;
        } else if(page == totalPage) { // 最后一页，则显示上一页，不显示下一页
            prePage = true;
            nextPage = false;
        }

        // 进行数据库的分页查询
        // 把获取到的数据，返回给前端即可
        // 这里使用一个双重锁机制，防止绕过redis直接访问我的数据库

        // 设置序列化（给Key设置序列化，否则在redis中显示出来的是乱码）
        redisTemplate.setKeySerializer(RedisUtils.setRedisSerializer());
        // 尝试从redis取出这个数据
        List<Blog> blogList = (List<Blog>) redisTemplate.opsForValue().get("blogList" + page);
        // 没有取出来的话，就访问数据库
        if(null == blogList) {
            // 把这个方法锁定，就是说，同时一万个人访问，第一个人会访问数据库，其他人都是访问的redis
            // 这样可能会让其他的人慢那么一点，但是会让我们的数据库负载一下变得很小
            synchronized(this) {
                blogList = (List<Blog>) redisTemplate.opsForValue().get("blogList" + page);
                if(null == blogList) {
                    blogList = blogService.queryByPageAndPageSize(page, pageSize);
                    // 在放入Redis的时候，直接给他设置了过期时间
                    // 四个参数的含义:                             key,value,过期时间,单位
                    redisTemplate.opsForValue().set("blogList" + page, blogList, 300, TimeUnit.SECONDS);
                }
            }
        }

        // 在这里参数准备完毕，我们直接返回前端数据，进行显示

        // 上一页和下一页的判断依据
        model.addAttribute("prePage", prePage);
        model.addAttribute("nextPage", nextPage);
        // 每页要显示的内容
        model.addAttribute("blogList", blogList);
        // 每页要显示的长度
        model.addAttribute("length", blogList.size() - 1);
        // 第几页
        model.addAttribute("page", page);
        // 共多少页
        model.addAttribute("totalPage", totalPage);
        // 页脚三兄弟
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("messageSize", messageSize);
        model.addAttribute("totalView", totalView);

        return "index";

        /*// 设置序列化编码
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

        return "index";*/
    }

    // 分类
    @GetMapping("/toTypes")
    public String toTypes(Model model, HttpServletRequest request) {

        // 页脚三兄弟
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("totalView", totalView);
        model.addAttribute("messageSize", messageSize);

        List<Foreignkey> foreignkeyList = foreignkeyDao.queryAll();
        model.addAttribute("foreignkeyList", foreignkeyList);

        boolean b = true;

        String label = request.getParameter("label");

        if(label == null) { // 这个是第一次访问的时候啥标签都没选择的时候要显示的东西

            List<Blog> blogList = blogService.queryAll();
            model.addAttribute("labelList", blogList);
            model.addAttribute("forNumber", 9);


        } else { // 走到这一步就是按照Lable查询
            List<Blog> labelList = blogService.queryByLabel(label);
            // 这种情况下，说明这个标签下面一篇文章都没有，则，直接不显示所有的即可
            if(labelList.size() == 0) {
                b = false;
                int forNumber = 0; // 这时候这个设置多少都无所谓了
                model.addAttribute("labelList", labelList);
                model.addAttribute("forNumber", forNumber);
            } else {
                b = true; // 需要显示出东西来，则返回给前端参数
                int forNumber = labelList.size() - 1;
                model.addAttribute("labelList", labelList);
                model.addAttribute("forNumber", forNumber);
            }
        }

        // 这个很重要不能丢掉
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

    // 添加跳转器，播放选中的电影
    @RequestMapping("/toShowVideo")
    public String toShowVideo(HttpServletRequest request, Model model) throws ClientException {

        String cover = request.getParameter("cover");

        // 通过封面图的地址，来查询整个需要的数据，因为阿里云储存的封面图的路径是独一无二的，所以才可以用到
        // 如果用name的话，name有可能会冲突，所以就选了Cover
        PlayVideo playVideo = videoService.queryByCover(cover);

        // 获取到videoId
        String videoId = playVideo.getVideoId();
        // 实时查询新的凭证
        String aliyunPlayAuth = MyAliyunUtils.getAliyunPlayAuth(videoId);

        if(playVideo == null) { // 啥都没查到，直接到404
            return "404";
        }
        // 查询到了，有结果，则传递到播放页面，直接显示出来
        // 有东西，则发送给前端
        model.addAttribute("cover", cover);
        model.addAttribute("videoId", playVideo.getVideoId());
        model.addAttribute("playAuth", aliyunPlayAuth);

        // 页脚三兄弟
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("messageSize", messageSize);
        model.addAttribute("totalView", totalView);

        // 页面跳转
        return "showVideo";
    }

































}
