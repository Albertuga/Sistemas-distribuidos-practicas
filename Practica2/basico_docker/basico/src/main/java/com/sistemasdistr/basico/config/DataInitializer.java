package com.sistemasdistr.basico.config;

import com.sistemasdistr.basico.model.Categoria;
import com.sistemasdistr.basico.model.Role;
import com.sistemasdistr.basico.model.User;
import com.sistemasdistr.basico.model.Videojuego;
import com.sistemasdistr.basico.repository.CategoriaRepository;
import com.sistemasdistr.basico.repository.RoleRepository;
import com.sistemasdistr.basico.repository.UserRepository;
import com.sistemasdistr.basico.repository.VideojuegoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoriaRepository categoriaRepository;
    private final VideojuegoRepository videojuegoRepository;

    // Actualizamos el constructor para incluir los nuevos repositorios
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, 
                           PasswordEncoder passwordEncoder, CategoriaRepository categoriaRepository, 
                           VideojuegoRepository videojuegoRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoriaRepository = categoriaRepository;
        this.videojuegoRepository = videojuegoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. INICIALIZAR ROLES Y USUARIO ADMIN
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setRoleName("ROLE_ADMIN");
            adminRole.setShowOnCreate(1);
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setRoleName("ROLE_USER");
            userRole.setShowOnCreate(1);
            roleRepository.save(userRole);

            if (userRepository.findUserByUsername("admin") == null) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("1234")); 
                adminUser.setNombreUsuario("Administrador");
                adminUser.setEmailuser("admin@test.com");
                adminUser.setFechaUltimoAcceso(LocalDateTime.now());
                adminUser.setUserRole(adminRole);
                userRepository.save(adminUser);
            }
        }

        // 2. PRECARGAR CATEGORÍAS Y JUEGOS
        if (categoriaRepository.count() == 0) {
            Categoria catRpg = new Categoria("RPG", "Juegos de rol y mundos abiertos.");
            Categoria catAccion = new Categoria("Acción", "Adrenalina y disparos.");
            categoriaRepository.save(catRpg);
            categoriaRepository.save(catAccion);

            if (videojuegoRepository.count() == 0) {
                Videojuego v1 = new Videojuego();
                v1.setTitulo("Cyberpunk 2077");
                v1.setPrecio(49.99);
                v1.setStock(15);
                v1.setSinopsis("Ciudad futurista llena de neones y peligros.");
                v1.setCategoria(catRpg);
                v1.setImagenUrl("https://static.wikia.nocookie.net/playstation5617/images/8/81/CP77.png/revision/latest?cb=20220103200708&path-prefix=es");

                Videojuego v2 = new Videojuego();
                v2.setTitulo("DOOM Eternal");
                v2.setPrecio(19.99);
                v2.setStock(5);
                v2.setSinopsis("Acción frenética contra demonios de otra dimensión.");
                v2.setCategoria(catAccion);
                v2.setImagenUrl("https://image.api.playstation.com/vulcan/ap/rnd/202010/0114/ERNPc4gFqeRDG1tYQIfOKQtM.png");

                videojuegoRepository.save(v1);
                videojuegoRepository.save(v2);
                System.out.println("✅ Datos de prueba (Categorías y Juegos) precargados con éxito.");
            }
        }
    }
}