package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.sistemasdistr.basico.repository.UserRepository;
import java.security.Principal;

@Controller
public class Maincontroller {
    
	private final UserRepository userRepository;
	
	// Inyectamos el repositorio de usuarios
    public Maincontroller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
 // PANTALLA DE UBICACIÓN (GOOGLE MAPS)
    @GetMapping("/tiendas")
    public String vistaTiendas() {
        return "tiendas"; // Buscará tiendas.html en la carpeta templates
    }
	
    @GetMapping("/")
    public String vistaHome(ModelMap interfazConPantalla){
        return "index";
    }

    // ¡ESTE ES EL MÉTODO CLAVE PARA QUE SE VEA EL LOGIN!
    @GetMapping("/login")
    public String vistaLogin() {
        return "login"; // Esto le dice a Spring que busque el archivo login.html
    }
    
 // PANTALLA DE MI PERFIL
    @GetMapping("/perfil")
    public String vistaPerfil(ModelMap model, Principal principal) {
        // Buscamos en la BD los datos completos del usuario que tiene la sesión activa
        String usernameActual = principal.getName();
        User usuarioLogueado = userRepository.findUserByUsername(usernameActual);
        
        model.addAttribute("usuario", usuarioLogueado);
        return "perfil"; // Buscará perfil.html
    }
    
}