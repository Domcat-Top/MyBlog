package com.tom.dao;

import com.tom.pojo.Blog;
import com.tom.pojo.Foreignkey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
@Repository
@Transactional //添加事务回滚注解，防止误操作
public interface BlogDao {

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

    // 根据博客名称查找
    List<Blog> queryByTitle(String title);

    // 写入博客
    int addBlog(Blog blog);

    // 删除博客
    int deleteBlogById(int id);

    // 修改博客
    int alterBlog(Blog blog);

    // 给博客的浏览量加1
    int addView(int id);

    // 总的访问量
    int getTotalView();

    // 具体的分页查询
    List<Blog> queryByPageAndPageSize(@Param("page") int page, @Param("pageSize") int pageSize);

    // 查询发布最多类型的博客
    Foreignkey queryMaxLabelBlog();

    // 根据标签出现次数的多少，来排序发布最多的博客类型---倒序
    List<Foreignkey> queryByAppearTimesOfLabel();


}
