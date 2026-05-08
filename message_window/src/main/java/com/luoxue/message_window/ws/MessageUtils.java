package com.luoxue.message_window.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MessageUtils {
    public static String getMessage(boolean isSystemMessage, String fromName, String message) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("system", isSystemMessage);
        node.put("message", message);
        node.put("fromName", fromName);

        String json;
        try {
            json = mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
