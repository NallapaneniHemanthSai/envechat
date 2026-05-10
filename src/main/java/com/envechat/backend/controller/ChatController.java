package com.envechat.backend.controller;

import com.envechat.backend.dto.ChatMessage;
import com.envechat.backend.model.Message;
import com.envechat.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable String roomId,
                            @Payload ChatMessage chatMessage,
                            @AuthenticationPrincipal Principal principal) {

        if (principal == null) {
            log.warn("Unauthenticated message attempt on room {}", roomId);
            return;
        }

        String email = principal.getName();

        chatMessage.setRoomId(roomId);
        chatMessage.setSenderUsername(email); // override anything client sent

        chatService.saveMessage(chatMessage, email);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, chatMessage);
    }

    @MessageMapping("/chat/{roomId}/join")
    public void joinRoom(@DestinationVariable String roomId,
                         @Payload ChatMessage chatMessage,
                         SimpMessageHeaderAccessor headerAccessor,
                         @AuthenticationPrincipal Principal principal) {

        if (principal == null) {
            log.warn("Unauthenticated join attempt on room {}", roomId);
            return;
        }

        String email = principal.getName();

        // Store in session for disconnect events later
        headerAccessor.getSessionAttributes().put("username", email);
        headerAccessor.getSessionAttributes().put("roomId", roomId);

        chatMessage.setSenderUsername(email);
        chatMessage.setRoomId(roomId);
        chatMessage.setType(Message.MessageType.JOIN);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, chatMessage);
    }

    @GetMapping("/api/chat/{roomId}/history")
    public List<Message> getRoomHistory(@PathVariable String roomId) {
        return chatService.getRoomHistory(roomId);
    }
}