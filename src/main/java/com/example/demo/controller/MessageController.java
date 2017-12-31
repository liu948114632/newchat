package com.example.demo.controller;

import com.example.demo.service.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class MessageController {
    @Autowired
    private MessageManager messageManager ;
    @RequestMapping(value = "/getMessage", produces = "application/json")
    public  Object getMessage(HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin", "*");
        return messageManager.getMessages(); //返回50条消息
    }

    @RequestMapping(value = "/getOnline")
    public int getOnline(){
        return MySocket.getOnline_num();
    }
}
