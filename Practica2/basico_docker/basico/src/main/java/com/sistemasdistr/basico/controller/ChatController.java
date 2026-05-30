package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {
	// 1. Mostrar la página HTML del chat
    @GetMapping("/chat")
    public String mostrarChat() {
        return "chat"; 
    }

    // 2. Recibir un mensaje y enviarlo a todos (/topic/public)
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    // 3. Avisar cuando un usuario entra a la sala
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }
}
