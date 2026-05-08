package com.luoxue.message_window.ws;

import com.luoxue.message_window.config.GetHttpSessionConfig;
import com.luoxue.message_window.domain.Messages;
import org.apache.logging.log4j.util.MessageSupplier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfig.class)
@Component
public class ChatEndpoint {

    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();
    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        //由于登录模块是静态页面，这里需要让websocket链接时带上user用户信息
        Map<String, List<String>> params = session.getRequestParameterMap();
        List<String> userIdList = params.get("userId");
        if (userIdList != null && !userIdList.isEmpty()) {
            String userId = userIdList.get(0);
            // 存储该用户对应的 Session
            onlineUsers.put(userId, session);
            // 绑定到 session 的用户属性中
            session.getUserProperties().put("userId", userId);
            String message = MessageUtils.getMessage(true, null, getFriend());
            broadCastAllUser(message);
        } else {
            // 未提供 userId 则关闭连接
            try { session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Missing userId")); } catch (
                    IOException e) {}
        }
    }

    private String getFriend(){
        Set<String> set = onlineUsers.keySet();
        StringBuilder sb = new StringBuilder("[");
        for (String id : set) {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append("\"").append(id).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    private void broadCastAllUser(String message){
        Set<Map.Entry<String, Session>> entries = onlineUsers.entrySet();
        for (Map.Entry<String, Session> entry : entries) {
            Session session = entry.getValue();
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @OnMessage
    public void onMessage(Messages message){
        String userId = String.valueOf(message.getReceiverId());
        Session session = onlineUsers.get(userId);
        String mess = MessageUtils.getMessage(false, userId, message.getContent());
        try {
            session.getBasicRemote().sendText(mess);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @OnClose
    public void onClose(Session session){
        Map<String, List<String>> params = session.getRequestParameterMap();
        List<String> userIdList = params.get("userId");
        String userId = userIdList.get(0);
        onlineUsers.remove(userId);

        String message = MessageUtils.getMessage(true, null, getFriend());
        broadCastAllUser(message);
    }

}
