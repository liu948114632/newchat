package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/29
 */
@Service
public class UserManage {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private Map<String,User> userMap = new HashMap();

    public User getUserInfo(int userid) {
        try{
            User user = userMap.get(userid+"");
            Date nowDate = null;
            if(user != null ){
                nowDate = user.getValidTime();
            }
            if(nowDate != null && (new Date().getTime() - nowDate.getTime())  < 5 * 60 * 1000){
                return userMap.get(userid+"");
            }else{
                user = getUserDb(userid);
                if(user == null){
                    return null;
                }
                user.setValidTime(new Date());
                userMap.put(userid+"",user);
                return userMap.get(userid+"");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    class User {
        private int id;
        private String name;
        private Date validTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getValidTime() {
            return validTime;
        }

        public void setValidTime(Date validTime) {
            this.validTime = validTime;
        }
    }

    private User getUserDb(int id){
        Map map = jdbcTemplate.queryForMap("SELECT fNickName FROM fuser WHERE fid = "+id);
        if(map == null){
            return null;
        }
        User user = new User();
        user.setName(map.get("fNickName").toString());
        return user;
    }
}
