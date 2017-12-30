package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/29
 */
@Component
public class UserManage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static Map<Integer,User> userMap = new HashMap<>();

    private Set<String> keyWord = new HashSet<>();
    public  User getUserInfo(int userId) {
        try{
            User user = userMap.get(userId);
            Date nowDate = null;
            if(user != null ){
                nowDate = user.getValidTime();
            }
            if(nowDate != null && (new Date().getTime() - nowDate.getTime())  < 5 * 60 * 1000){
                return userMap.get(userId);
            }else{
                user = getUserDb(userId);
                if(user == null){
                    return null;
                }
                user.setValidTime(new Date());
                userMap.put(userId,user);
                return user;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private  User getUserDb(int id){
        Map map = jdbcTemplate.queryForMap("SELECT fNickName, floginName FROM fuser WHERE fid = "+id);
        if(map == null){
            return null;
        }
        User user = new User();
        if(map.get("fNickName")!= null){
            user.setName(map.get("fNickName").toString());
        }else {
            user.setName(map.get("floginName").toString());
        }
        return user;
    }

    public  String checkKeyWord(String message){
        String newMessage = message;
        for (String s :keyWord){
            if(message.contains(s)){
                newMessage =  message.replace(s,"**");
                break;
            }
        }
        return newMessage;
    }

    @PostConstruct
    @Scheduled(cron = "0 0/3 * * * ? ")
    public void syncKenWord(){
        Map map = jdbcTemplate.queryForMap("SELECT *  FROM fsystemargs s where s.`fKey` = 'keyWord'");
        String value;
        if(map != null && map.get("fValue")!= null){
            value = map.get("fValue").toString();
            String keys[] = value.split(",");
            Collections.addAll(this.keyWord, keys);
        }
    }
}
