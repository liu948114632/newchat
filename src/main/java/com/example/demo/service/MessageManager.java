package com.example.demo.service;

import com.example.demo.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageManager {
    @Value("${message.count}")
    private int count;

    private static List<Message> messages = new ArrayList<>(50);

    public List<Message> getMessages() {
        return messages;  //list集合，后进来的序号大。也就是返回的消息中第一条是最早发的。
    }

    public  void setMessage(Message message) {
        if(messages.size() == count){
            messages.remove(0); //删除第一条
            messages.add(message);
        }else {
            messages.add(message);
        }
    }


}
