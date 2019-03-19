package com.tlmb.net.servers.socket;

import com.google.gson.Gson;
import com.tlmb.net.servers.utils.CommonUtils;
import com.tlmb.net.servers.utils.TimeUtil;
import org.json.JSONObject;

import javax.websocket.Session;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerManager {
    private ServerManager() {

    }

    private static class Holder {
        public static final ServerManager INSTANCE = new ServerManager();
    }

    public static ServerManager getInstance() {
        return Holder.INSTANCE;
    }

    private ConcurrentHashMap<String, Session> socketSession = new ConcurrentHashMap<>();

    public void addSocket(String userId, Session session) {
        socketSession.put(userId, session);
    }

    public void removeSocket(String userId) {
        socketSession.remove(userId);
    }

    public synchronized void broadcast(String userId, String message) {
        MessageBean msg = getMessage(message);
        msg.setFromUserId(userId);
        msg.setTime(TimeUtil.stampToDate(TimeUtil.getUtcTimeMills()));
        Gson gson = new Gson();
        if (CommonUtils.isTextEmpty(msg.getToUserId())) {
            // 群播
            Iterator<Map.Entry<String, Session>> iterator = socketSession.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Session> testEntry = iterator.next();
                String selfUserId = testEntry.getKey();
                Session session = testEntry.getValue();
                if ((selfUserId != null) && (!selfUserId.equals(msg.getFromUserId()))) {
                    // 不发给自己
                    sendMsg(session, gson.toJson(msg));
                }
            }
        } else {
            // 单播
            Session session = socketSession.get(msg.getToUserId());
            if (session != null) {
                sendMsg(session, gson.toJson(msg));
            }
        }
    }

    private synchronized MessageBean getMessage(String message) {
        MessageBean messageBean = new MessageBean();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
        } catch (Exception e) {
        }
        if (jsonObject != null) {
            messageBean.setMessage(jsonObject.optString("message", ""));
            messageBean.setToUserId(jsonObject.optString("toUserId", ""));
        } else {
            messageBean.setMessage(message);
        }
        return messageBean;
    }

    private synchronized void sendMsg(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
