package com.tom.dao;


import com.tom.pojo.Foreignkey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ForeignkeyDao {

    // 查询全部的标签
    public List<Foreignkey> queryAll();

    // 按照ID查询
    public List<Foreignkey> queryById(int id);

    // 删除标签
    public int deleteLabel(int id);


}
