package com.tom.controller;

import com.tom.dao.ForeignkeyDao;
import com.tom.pojo.Blog;
import com.tom.pojo.Foreignkey;
import com.tom.pojo.User;
import com.tom.service.BlogService;
import com.tom.service.MessageService;
import com.tom.service.UserService;
import com.tom.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {

    private static int blogSize;
    private static int messageSize;

    @Autowired
    BlogService blogService;
    @Autowired
    ForeignkeyDao foreignkeyDao;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;


    // 去往登录页面
    @RequestMapping({"/toLogin", "/login"})
    public String toLogin() {
        return "admin/login";
    }

    // 后台登录校验
    @GetMapping("/checkLogin")
    public String checkLogin(Model model, HttpServletRequest request, HttpSession session) {

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        User user = userService.checkLogin(new User(userName, password));

        if(user == null) {
            model.addAttribute("msg", "用户名或密码错误");
            return "admin/login";
        } else {
            session.setAttribute("currentUser", userName);
            return "redirect:/admin/toAdminIndex";
        }

    }

    // 注销
    @RequestMapping("/toLogout")
    public String toLogout(HttpSession session) {

        if(session.getAttribute("currentUser") != null) {
            session.removeAttribute("currentUser");

        }
        return "redirect:/";
    }

    // 后台首页
    @RequestMapping({"/admin/toAdminIndex", "/admin", "/admin/index"})
    public String toAdminIndex(Model model) {

        blogSize = blogService.queryAll().size();
        messageSize = messageService.queryAll().size();
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("messageSize", messageSize);

        return "admin/index";
    }

    // 写博客页面
    @RequestMapping("/admin/toBlogWrite")
    public String toBlogWrite(Model model, HttpServletRequest request) {
        blogSize = blogService.queryAll().size();
        messageSize = messageService.queryAll().size();
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("messageSize", messageSize);

        List<Foreignkey> foreignkeyList = foreignkeyDao.queryAll();

        model.addAttribute("foreignkeySize", foreignkeyList.size());

        model.addAttribute("foreignkeyList", foreignkeyList);

        return "admin/blogs-input";
    }

    // 标签管理
    @GetMapping("/admin/toTypes")
    public String toTypes(Model model) {
        blogSize = blogService.queryAll().size();
        messageSize = messageService.queryAll().size();
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("messageSize", messageSize);

        model.addAttribute("msg", "操作成功！");

        List<Foreignkey> foreignkeyList = foreignkeyDao.queryAll();

        model.addAttribute("foreignkeyList", foreignkeyList);

        return "admin/types";

    }

    // 删除标签操作
    @Transactional
    @PostMapping("/admin/toDeleteLabel")
    public String toDeleteLabel(Model model, HttpServletRequest request) {

        String id = request.getParameter("id");

        int i = foreignkeyDao.deleteLabel(Integer.parseInt(id));

        if(i != 0) {
            model.addAttribute("msg", "操作成功！");
        } else {
            model.addAttribute("msg", "操作失败！");
        }
        return "redirect:/admin/toTypes";
    }

    // 执行写入数据库操作
    @Transactional
    @PostMapping("/admin/writeBlog")
    public String writeBlog(Model model, HttpServletRequest request) {

        String flag = request.getParameter("flag");

        String title = request.getParameter("title");

        String content = request.getParameter("content");

        String label = request.getParameter("label");

        String picture = request.getParameter("picture");

        String description = request.getParameter("description");

        int result = blogService.addBlog(new Blog(title, label, description, content, picture, flag));

        if(result == 0) {
            return "error/500";
        } else {
            // 写博客成功了，自动的给他生成一个二维码文件，放在服务器上面
            String codeContent = "http://101.200.78.148:8888/toBlog?id=" + blogService.queryByTitle(title).get(0).getId();
            String path = "/home/blog/QRCode";
            QRCodeUtils.fileName = "" + blogService.queryByTitle(title).get(0).getId();
            boolean b = QRCodeUtils.codeCreate(codeContent, path, 150, null);
            return "redirect:/admin/toBlogs";
        }

    }

    // 去往博客总体管理页面
    @GetMapping("/admin/toBlogs")
    public String toBlogs(Model model, HttpServletRequest request) {
        blogSize = blogService.queryAll().size();
        messageSize = messageService.queryAll().size();
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("messageSize", messageSize);

        String title = request.getParameter("title");

        List<Blog> blogList;

        if(title == null) {
            blogList = blogService.queryAll();
        } else {
            blogList = blogService.queryByTitle(title);
            if(blogList.size() == 0) {
                blogList = blogService.queryAll();
            }
        }

        model.addAttribute("blogList", blogList);

        return "admin/blogs";
    }



    // 去往博客编辑页面
    private static String id;
    @GetMapping("/admin/toChangeBlog")
    public String toChangeBlog(Model model, HttpServletRequest request) {
        blogSize = blogService.queryAll().size();
        messageSize = messageService.queryAll().size();
        model.addAttribute("blogSize", blogSize);
        model.addAttribute("messageSize", messageSize);

        id = request.getParameter("id");

        Blog blog = blogService.queryByID(Integer.parseInt(id));

        model.addAttribute("blog", blog);

        model.addAttribute("foreignkeyList", foreignkeyDao.queryAll());

        return "admin/changeBlog";

    }

    // 删除博客
    @RequestMapping("/admin/toDelete")
    public String toDeleteBlog(HttpServletRequest request) {

        String id = request.getParameter("id");

        // 现查询出这个ID对应的标签是谁
        List<Foreignkey> foreignkeyList = foreignkeyDao.queryById(Integer.parseInt(id));

        // 删除之前先查询是否在这个标签下面还有博客，如果有的话，就不让删除
        List<Blog> blogList = blogService.queryByLabel(foreignkeyList.get(0).label);

        if(blogList.size() > 0) { // 大于0，说明这个容器里面有东西，也就是这个标签下面还有博客，不允许删除
            return "error/500"; // 直接报错500
        }

        int result = foreignkeyDao.deleteLabel(Integer.parseInt(id));
        if(result == 0) {
            return "error/500";
        } else {
            return "redirect:/admin/toTypes";
        }

    }

    // 修改博客
    @PostMapping("/admin/alterBlog")
    @Transactional
    public String alterBlog(HttpServletRequest request) {

        String flag = request.getParameter("flag");

        String title = request.getParameter("title");

        String content = request.getParameter("content");

        String label = request.getParameter("label");

        String picture = request.getParameter("picture");

        String description = request.getParameter("description");

        int result = blogService.alterBlog(new Blog(Integer.parseInt(id), title, label, description, content, picture, flag));

        // 再把这个改回来，之前是为了以前的操作方便些的，这次是为了代码的简便
        return "redirect:/admin/toBlogs";

    }

    // 去往增加标签页面
    @RequestMapping("/admin/toAddLabel")
    public String toAddLabel() {
        return "admin/types-input";
    }

    // 执行标签添加的操作
    @RequestMapping("/admin/addLabel")
    public String addLabel(HttpServletRequest request) {

        // 获取标签的名称
        String labelId = request.getParameter("name");

        // 执行写入数据库的操作
        int i = foreignkeyDao.addLabel(labelId);

        if(i >= 1) {
            // 添加成功
            return "redirect:/admin/toTypes";
        } else {
            // 报错500
            return "redirect:/500";
        }


    }

    // 去往标签的修改页面---顺手把那个标签的ID发过去
    private static String labelId;
    @RequestMapping("/admin/toChangeLabel")
    public String toChangeLabel(HttpServletRequest request, Model model) {

        labelId = request.getParameter("id");

        // 通过ID获取到这个标签的name
        List<Foreignkey> foreignkeyList = foreignkeyDao.queryById(Integer.parseInt(request.getParameter("id")));
        model.addAttribute("foreignkeyList", foreignkeyList);
        return "admin/types-change";
    }

    // 标签的修改
    @RequestMapping("/admin/changeLabel")
    public String aboutLabel(HttpServletRequest request) {

        String name = request.getParameter("name");

        // 执行修改操作写入数据库
        int i = foreignkeyDao.alterLabel(new Foreignkey(Integer.parseInt(labelId), name));

        if(i >= 1) {
            // 修改成功
            return "redirect:/admin/toTypes";
        } else {
            return "500";
        }

    }
























}
