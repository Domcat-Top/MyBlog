package com.tom.service;

import com.tom.dao.BlogDao;
import com.tom.pojo.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    BlogDao blogDao;

    @Override
    public List<Blog> queryAll() {
        return blogDao.queryAll();
    }

    @Override
    public List<Blog> queryByPage(int pageNumber) {
        return blogDao.queryByPage(pageNumber);
    }

    @Override
    public List<Blog> queryByLabel(String label) {
        return blogDao.queryByLabel(label);
    }

    @Override
    public List<Blog> queryByLike(String question) {
        return blogDao.queryByLike(question);
    }

    @Override
    public Blog queryByID(int id) {
        return blogDao.queryByID(id);
    }

    @Override
    public int addBlog(Blog blog) {
        return blogDao.addBlog(blog);
    }

    @Override
    public List<Blog> queryByTitle(String title) {
        return blogDao.queryByTitle(title);
    }

    @Override
    public int deleteBlogById(int id) {
        return blogDao.deleteBlogById(id);
    }

    @Override
    public int alterBlog(Blog blog) {
        return blogDao.alterBlog(blog);
    }

    @Override
    public int addView(int id) {
        return blogDao.addView(id);
    }
    

    @Override
    public int getTotalView() {
        return blogDao.getTotalView();
    }


}
