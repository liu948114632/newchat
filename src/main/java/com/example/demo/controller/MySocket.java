package com.example.demo.controller;

import com.example.demo.config.SpringUtils;
import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.service.MessageManager;
import com.example.demo.service.UserManage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
@Component
@ServerEndpoint(value = "/websocket")  //该注解表示该类被声明为一个webSocket终端
public class MySocket {
    private static final Logger LOGGER=Logger.getLogger(MySocket.class);
    //初始在线人数
    private static int online_num = 0;
    //线程安全的socket集合
    static CopyOnWriteArraySet<Session> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        webSocketSet.add(session);
        addOnlineCount();
        LOGGER.debug("有链接加入，当前人数为:"+getOnline_num());
    }
    @OnClose
    public void onClose(Session session){
        webSocketSet.remove(session);
        subOnlineCount();
        LOGGER.debug("有链接关闭,当前人数为:"+getOnline_num());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        UserManage userManage = (UserManage) SpringUtils.getBean("userManage") ;
        MessageManager messageManager = (MessageManager) SpringUtils.getBean("messageManager") ;
        //会话
        LOGGER.debug("来自客户端的消息:"+message);
        try {
            if(message == null || message.trim().length() ==0 ){
                return;
            }
            String s[] = message.split("##");
            if(s.length!=2){
                return;
            }
            String real = s[0];
            if("".equals(real) || real == null){
                return;
            }
            int id = Integer.valueOf(s[1]);
            String userName ;
            if(id !=-1){
                User user = userManage.getUserInfo(id);
                if(user !=null ){
                    userName = user.getName();
                }else {
                    userName = "游客"+ getName(session.getId());
                }
            }else {
                userName = "游客"+ getName(session.getId());
            }
            Date now = new Date();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
            for (Session item:webSocketSet){
                item.getAsyncRemote().sendText(userName+"$" +userManage.checkKeyWord(real)+"$"+time.substring(11,19));
            }
            //异步将消息放入内存
            getExecutor().execute(() -> {
                Message message1 = new Message(userName,real, time);
                messageManager.setMessage(message1);
            });
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

    }

    private int getName(String id){
        char c[] = id.toCharArray();
        int num = 10086;
        for (char c1 :c){
            num += c1;
        }
        return num;
    }
    static int getOnline_num(){
        return MySocket.online_num;
    }
    private synchronized void subOnlineCount(){
         MySocket.online_num--;
    }
    private synchronized void addOnlineCount(){
        MySocket.online_num++;
    }

    private static Executor getExecutor(){
        return Executors.newFixedThreadPool(100);
    }

}
