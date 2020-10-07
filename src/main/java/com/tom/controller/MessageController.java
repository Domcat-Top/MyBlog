package com.tom.controller;

import com.tom.pojo.Message;
import com.tom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/toAddMessage")
    @Transactional
    public String toAddMessage(Model model, HttpServletRequest request) {

        String content = request.getParameter("content");
        String nickName = request.getParameter("nickName");
        String email = request.getParameter("email");

        int result = messageService.addMessage(new Message(nickName, email, content));

        return "redirect:/toMessage";
    }
}
