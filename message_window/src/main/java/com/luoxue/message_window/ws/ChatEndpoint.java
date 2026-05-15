package com.luoxue.message_window.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luoxue.message_window.domain.Messages;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat")
@Component
public class ChatEndpoint {

    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();
    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        try {
            System.out.println("=== WebSocket onOpen 被调用，session id: " + session.getId());

            this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            System.out.println("获取到的 HttpSession: " + this.httpSession);

            Map<String, List<String>> params = session.getRequestParameterMap();
            System.out.println("请求参数: " + params);
            List<String> userIdList = params.get("userId");
            System.out.println("userIdList: " + userIdList);

            if (userIdList != null && !userIdList.isEmpty()) {
                String userId = userIdList.get(0);
                System.out.println("解析到的 userId: " + userId);

                onlineUsers.put(userId, session);
                session.getUserProperties().put("userId", userId);
                System.out.println("当前在线用户数: " + onlineUsers.size());

                String message = MessageUtils.getMessage(true, null, getFriend());
                System.out.println("广播消息内容: " + message);
                broadCastAllUser(message);
                System.out.println("广播完成");
            } else {
                System.out.println("没有 userId 参数，准备关闭连接");
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Missing userId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, e.getMessage()));
            } catch (IOException ignored) {}
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
    public void onMessage(String messageText, Session session) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Messages message = mapper.readValue(messageText, Messages.class);

            String fromUserId = (String) session.getUserProperties().get("userId");
            String toUserId = String.valueOf(message.getReceiverId());

            Session targetSession = onlineUsers.get(toUserId);
            if (targetSession != null && targetSession.isOpen()) {
                String response = MessageUtils.getMessage(false, fromUserId, message.getContent());
                targetSession.getBasicRemote().sendText(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
