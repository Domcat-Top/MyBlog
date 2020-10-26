package com.tom.service;

import com.tom.dao.ForeignkeyDao;
import com.tom.pojo.Foreignkey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForeignkeyServiceImpl implements ForeignkeyService {

    @Autowired
    ForeignkeyDao labelDao;

    @Override
    public List<Foreignkey> queryAll() {
        return labelDao.queryAll();
    }

    @Override
    public List<Foreignkey> queryById(int id) {
        return labelDao.queryById(id);
    }

    @Override
    public int deleteLabel(int id) {
        return labelDao.deleteLabel(id);
    }

    @Override
    public int addLabel(String name) {
        return labelDao.addLabel(name);
    }

    @Override
    public int alterLabel(Foreignkey foreignkey) {
        return labelDao.alterLabel(foreignkey);
    }
}
