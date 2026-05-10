package com.envechat.backend.service;

import com.envechat.backend.dto.ChatMessage;
import com.envechat.backend.model.Message;
import com.envechat.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;

    public Message saveMessage(ChatMessage chatMessage, String email) {

        Message message = new Message();

        message.setContent(chatMessage.getContent());
        message.setRoomId(chatMessage.getRoomId());
        message.setType(chatMessage.getType());
        
        message.setSenderUsername(email);

        return messageRepository.save(message);
    }

    public List<Message> getRoomHistory(String roomId) {
        return messageRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }
}