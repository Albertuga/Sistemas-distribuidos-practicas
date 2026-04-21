package com.sistemasdistr.basico.config;

import com.sistemasdistr.basico.model.Role;
import com.sistemasdistr.basico.model.User;
import com.sistemasdistr.basico.repository.RoleRepository;
import com.sistemasdistr.basico.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Comprobamos si el usuario ya existe en la base de datos
        if (userRepository.findUserByUsername("admin") == null) {
            
            // 1. Creamos el Rol de Administrador
            Role adminRole = new Role();
            adminRole.setRoleName("ROLE_ADMIN");
            adminRole.setShowOnCreate(1);
            roleRepository.save(adminRole);

            // 2. Creamos el Usuario
            User adminUser = new User();
            adminUser.setUsername("admin");
            // Usamos el PasswordEncoder para guardar la contraseña de forma segura
            adminUser.setPassword(passwordEncoder.encode("1234")); 
            adminUser.setNombreUsuario("Administrador");
            adminUser.setEmailuser("admin@test.com");
            adminUser.setFechaUltimoAcceso(LocalDateTime.now());
            adminUser.setUserRole(adminRole);

            userRepository.save(adminUser);
            System.out.println("✅ Usuario 'admin' con contraseña '1234' creado con éxito en la BD.");
        }
    }
}