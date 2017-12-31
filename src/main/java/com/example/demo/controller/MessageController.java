package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.service.MessageManager;
import com.example.demo.service.UserManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private MessageManager messageManager ;
    @Autowired
    private UserManage userManage;

    @RequestMapping(value = "/getMessage", produces = "application/json")
    public  Object getMessage(HttpServletResponse response,
                              @RequestParam(defaultValue = "-1",required = false) int real){
        response.addHeader("Access-Control-Allow-Origin", "*");
        List<Message> messages = messageManager.getMessages();
        List<Message> result =new ArrayList<>();
        result.addAll(messages);
        if(real == -1){
            result.forEach(message -> message.setMessage(userManage.checkKeyWord(message.getMessage())));
            return result;
        }else {
            return messages; //返回50条消息
        }
    }

    @RequestMapping(value = "/getOnline")
    public int getOnline(){
        return MySocket.getOnline_num();
    }

    /**
     *
     * 主动同步数据库中
     */
    @RequestMapping(value = "/syncKeyword")
    public void syncKeyword(){
        userManage.syncKenWord();
    }
}
