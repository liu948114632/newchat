package com.example.demo.controller;

import com.example.demo.service.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @Autowired
    private MessageManager messageManager ;
    @RequestMapping("/getMessage")
    public Object getMessage(){
        return messageManager.getMessages(); //返回50条消息
    }

    @RequestMapping("/getOnline")
    public int getOnline(){
        return MySocket.getOnline_num();
    }
}
