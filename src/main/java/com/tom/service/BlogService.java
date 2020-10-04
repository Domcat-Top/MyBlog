package com.tom.service;

import com.tom.pojo.Blog;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BlogService {

    // 查询所有的内容
    List<Blog> queryAll();

    // 按照页数查询
    List<Blog> queryByPage(int pageNumber);

    // 按照标签查找
    List<Blog> queryByLabel(String label);

    // 导航栏查询--模糊查询
    List<Blog> queryByLike(String question);

    // 根据ID查询
    Blog queryByID(int id);

    // 写入博客
    int addBlog(Blog blog);

    // 根据博客名称查找
    List<Blog> queryByTitle(String title);

    // 删除博客
    int deleteBlogById(int id);

    // 修改博客
    int alterBlog(Blog blog);

    // 给博客的浏览量加1
    int addView(int id);

    // 总的访问量
    int getTotalView();





}
