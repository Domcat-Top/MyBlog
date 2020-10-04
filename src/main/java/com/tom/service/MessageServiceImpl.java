package com.tom.service;

import com.tom.dao.MessageDao;
import com.tom.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    MessageDao messageDao;

    @Override
    public List<Message> queryAll() {
        return messageDao.queryAll();
    }

    @Override
    public int addMessage(Message message) {
        return messageDao.addMessage(message);
    }
}
