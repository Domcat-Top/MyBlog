package com.tom.service;

import com.tom.pojo.Blog;
import com.tom.pojo.Foreignkey;

import java.util.List;

public interface ForeignkeyService {

    // 查询全部的标签
    public List<Foreignkey> queryAll();

    // 按照ID查询
    public List<Foreignkey> queryById(int id);

    // 删除标签
    public int deleteLabel(int id);

}
