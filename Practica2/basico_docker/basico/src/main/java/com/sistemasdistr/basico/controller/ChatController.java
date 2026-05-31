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

    // 1. Mostrar la página HTML del chat (FILTRO EN LA PUERTA)
    @GetMapping("/chat")
    public String mostrarChat(Principal principal) {
        User usuario = userRepository.findUserByUsername(principal.getName());
        
        if (usuario != null && usuario.isBaneado()) {
            return "redirect:/?error=baneado"; 
        }
        
        return "chat"; 
    }

    // 2. Recibir un mensaje y enviarlo a todos (FILTRO EN EL TÚNEL EN TIEMPO REAL)
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, Principal principal) {
        // Consultamos la BD en el milisegundo exacto en que llega el mensaje
        User usuario = userRepository.findUserByUsername(principal.getName());
        
        if (usuario != null && usuario.isBaneado()) {
            // Si está baneado, lanzamos un error interno que aborta la función. 
            // El mensaje muere aquí y NUNCA llega al resto de usuarios.
            throw new RuntimeException("Usuario baneado intentó enviar un mensaje.");
        }
        
        return chatMessage;
    }

    // 3. Avisar cuando un usuario entra a la sala (FILTRO DE CONEXIÓN)
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, Principal principal) {
        User usuario = userRepository.findUserByUsername(principal.getName());
        
        if (usuario != null && usuario.isBaneado()) {
            throw new RuntimeException("Usuario baneado intentó unirse al chat.");
        }
        
        return chatMessage;
    }
}