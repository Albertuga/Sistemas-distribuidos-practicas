package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.model.ChatMessage;
import com.sistemasdistr.basico.model.User;
import com.sistemasdistr.basico.repository.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ChatController {

    private final UserRepository userRepository;

    public ChatController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Mostrar la página HTML del chat (CON FILTRO DE BANEO)
    @GetMapping("/chat")
    public String mostrarChat(Principal principal) {
        User usuario = userRepository.findUserByUsername(principal.getName());
        
        // Si el usuario está baneado, lo expulsamos de vuelta al inicio con un mensaje
        if (usuario != null && usuario.isBaneado()) {
            return "redirect:/?error=baneado"; 
        }
        
        return "chat"; 
    }

    // 2. Recibir un mensaje y enviarlo a todos
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