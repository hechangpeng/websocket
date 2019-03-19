package com.tlmb.net.servers.socket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/{userId}")
public class SocketTest {

    @OnOpen
    public void onOpen(@PathParam(value = "userId") String userId, Session session) {
        System.out.println("onOpen " + userId);
        ServerManager.getInstance().addSocket(userId, session);
    }

    @OnClose
    public void onClose(@PathParam(value = "userId") String userId) {
        System.out.println("onClose " + userId);
        ServerManager.getInstance().removeSocket(userId);
    }

    @OnMessage
    public void onMessage(@PathParam(value = "userId") String userId, String message) {
        System.out.println("onMessage " + userId);
        System.out.println("onMessage: " + message);
        ServerManager.getInstance().broadcast(userId, message);
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("Error");
        error.printStackTrace();
    }
}
