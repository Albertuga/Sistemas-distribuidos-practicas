package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.model.User;
import com.sistemasdistr.basico.repository.RoleRepository;
import com.sistemasdistr.basico.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Read: Mostrar todos los usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", userRepository.findAll());
        return "usuarios";
    }

    // Create: Mostrar formulario de nuevo usuario
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new User());
        model.addAttribute("listaRoles", roleRepository.findAll());
        return "usuario_form";
    }

    // Create/Update: Guardar usuario
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") User usuario) {
        if (usuario.getId() == null) {
            // Usuario NUEVO: Encriptamos la contraseña y ponemos fecha de hoy
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuario.setFechaUltimoAcceso(LocalDateTime.now());
        } else {
            // Usuario EXISTENTE: Recuperamos el antiguo para no perder datos
            User usuarioExistente = userRepository.findById(usuario.getId()).orElseThrow();
            
            // Si el campo de contraseña viene vacío, mantenemos la antigua
            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                usuario.setPassword(usuarioExistente.getPassword());
            } else {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            usuario.setFechaUltimoAcceso(usuarioExistente.getFechaUltimoAcceso());
        }
        
        userRepository.save(usuario);
        return "redirect:/usuarios";
    }

    // Update: Cargar usuario para editar
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido: " + id));
        
        // Vaciamos el campo de la contraseña por seguridad en la vista HTML
        usuario.setPassword(""); 
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("listaRoles", roleRepository.findAll());
        return "usuario_form";
    }

    // Delete: Eliminar usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return "redirect:/usuarios";
    }
    
 // Ban / Unban: Cambiar el estado de acceso al chat
    @GetMapping("/estado/{id}")
    public String cambiarEstadoBaneo(@PathVariable Integer id) {
        User usuario = userRepository.findById(id).orElseThrow();
        
        // Protegemos para que el admin principal nunca pueda ser baneado por accidente
        if (!usuario.getUsername().equals("admin")) {
            usuario.setBaneado(!usuario.isBaneado());
            userRepository.save(usuario);
        }
        return "redirect:/usuarios";
    }
}
