package com.example.demo.config;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class HttpConfig extends Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request, HandshakeResponse response) {
        // TODO Auto-generated method stub
        sec.getUserProperties().put("ip",ipToString(request.getHeaders().get("host").get(0)));
    }

    private String ipToString(String ip){
        char c[] = ip.toCharArray();
        int num = 0;
        for (char c1 :c){
            num += (int) c1;
        }
        return num+"";
    }
}
