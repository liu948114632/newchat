package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.io.IOException;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/8/16
 */
@Controller
public class IndexController {
    @RequestMapping("/index")
    public String index(){
        return "socket";
    }

//后台主动给前台发送消息

//getAsyncRemote该方法是异步，当遍历给所有用户发送消息时，
// 如果上一次的消息没有发送完，再发下一次的会抛异常
//The remote endpoint was in state [TEXT_FULL_WRITING] which is an invalid state for called method
//两种解决方法，第一种是使用同步的方法getBasicRemote(),
// 第二种是在每一次推送给所有的用户后，使用thread.sleep(),保证第一次的推送完毕
    //同步的方法耗时，使用thread.sleep()也需要耗时，时间还不固定
    @RequestMapping("/test")
    @ResponseBody
    public void  test() throws InterruptedException, IOException {
        for (int i=1;i<10;i++){
            for(Session item:MySocket.webSocketSet){
                item.getBasicRemote().sendText("第"+i+"次消息推送");
//                item.session.getAsyncRemote().sendText("第"+i+"次消息推送");
            }
//            Thread.sleep(100);
        }
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request){
        String name=request.getParameter("name");
        return "index";
    }

}
