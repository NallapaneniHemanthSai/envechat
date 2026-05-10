package com.envechat.backend.dto;

import com.envechat.backend.model.Message.MessageType;
import lombok.Data;

@Data
public class ChatMessage {
    private String content;
    private String senderUsername;
    private String roomId;
    private MessageType type;
}