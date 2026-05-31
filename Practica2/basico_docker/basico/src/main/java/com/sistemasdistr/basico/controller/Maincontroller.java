package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.model.Role;
import com.sistemasdistr.basico.model.User;
import com.sistemasdistr.basico.repository.RoleRepository;
import com.sistemasdistr.basico.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class Maincontroller {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Maincontroller(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String vistaHome() {
        return "index";
    }

    @GetMapping("/login")
    public String vistaLogin() {
        return "login"; 
    }

    @GetMapping("/perfil")
    public String vistaPerfil(ModelMap model, Principal principal) {
        String usernameActual = principal.getName();
        User usuarioLogueado = userRepository.findUserByUsername(usernameActual);
        model.addAttribute("usuario", usuarioLogueado);
        return "perfil";
    }

    @GetMapping("/tiendas")
    public String vistaTiendas() {
        return "tiendas"; 
    }

    // --- NUEVAS RUTAS DE REGISTRO PÚBLICO ---

    @GetMapping("/registro")
    public String vistaRegistro(ModelMap model) {
        model.addAttribute("usuario", new User());
        return "registro"; // Muestra la pantalla de registro
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("usuario") User usuario) {
        // 1. Buscamos el rol de usuario normal para asignárselo
        Role userRole = null;
        for (Role role : roleRepository.findAll()) {
            if ("ROLE_USER".equals(role.getRoleName())) {
                userRole = role;
                break;
            }
        }

        // 2. Configuramos los datos automáticos
        usuario.setUserRole(userRole);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFechaUltimoAcceso(LocalDateTime.now());
        usuario.setBaneado(false);

        // 3. Guardamos y redirigimos al login con un mensaje de éxito
        userRepository.save(usuario);
        return "redirect:/login?registrado";
    }
}