package com.example.demo.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket")  //该注解表示该类被声明为一个webSocket终端
public class MySocket {
    private String name;
    private static final Logger LOGGER=Logger.getLogger(MySocket.class);
    //初始在线人数
    private static int online_num = 0;
    //线程安全的socket集合
    static CopyOnWriteArraySet<MySocket> webSocketSet = new CopyOnWriteArraySet<>();
    //会话
    Session session;
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        this.name=session.getId();
        webSocketSet.add(this);
        addOnlineCount();
        LOGGER.info("有链接加入，当前人数为:"+getOnline_num());
    }
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        LOGGER.info("有链接关闭,当前人数为:"+getOnline_num());
    }

    @OnMessage
    public void onMessage(String message) throws IOException{
        LOGGER.info("来自客户端的消息:"+message);
        if(message.startsWith("name")){
            this.name=message.substring(4,message.length());
        }else {
            if (message.startsWith("@")){
                //单发
                String s[]= message.split(":");
                String name=s[0].substring(1,s[0].length());
                String newMessage=s[1];
                for (MySocket item:webSocketSet){
                    if(item.name.equals(name)){
                        item.session.getAsyncRemote().sendText(newMessage);
                        LOGGER.info("单人发送");
                        break;
                    }
                }
            }else {
                //群发
                for(MySocket item:webSocketSet){
                    item.session.getAsyncRemote().sendText(message);
                }
            }
        }


    }
    private synchronized int getOnline_num(){
        return MySocket.online_num;
    }
    private synchronized void subOnlineCount(){
         MySocket.online_num--;
    }
    private synchronized void addOnlineCount(){
        MySocket.online_num++;
    }
}
