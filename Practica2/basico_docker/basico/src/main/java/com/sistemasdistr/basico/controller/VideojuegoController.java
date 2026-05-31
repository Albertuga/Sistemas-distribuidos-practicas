package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.model.Videojuego;
import com.sistemasdistr.basico.repository.CategoriaRepository;
import com.sistemasdistr.basico.repository.VideojuegoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/videojuegos")
public class VideojuegoController {

	private final VideojuegoRepository videojuegoRepository;
    private final CategoriaRepository categoriaRepository;

    // Inyectamos ambos repositorios
    public VideojuegoController(VideojuegoRepository videojuegoRepository, CategoriaRepository categoriaRepository) {
        this.videojuegoRepository = videojuegoRepository;
        this.categoriaRepository = categoriaRepository;
    }

 // Read: Mostrar todos los videojuegos o filtrar
    @GetMapping
    public String listarVideojuegos(@RequestParam(value = "buscar", required = false) String buscar, Model model) {
        if (buscar != null && !buscar.isEmpty()) {
            model.addAttribute("videojuegos", videojuegoRepository.findByTituloContainingIgnoreCase(buscar));
        } else {
            model.addAttribute("videojuegos", videojuegoRepository.findAll());
        }
        model.addAttribute("buscar", buscar);
        return "videojuegos";
    }

    // Create: Mostrar formulario
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("videojuego", new Videojuego());
        // Pasamos la lista de categorías para el desplegable
        model.addAttribute("listaCategorias", categoriaRepository.findAll()); 
        return "videojuego_form";
    }

    // Create/Update: Guardar en BD
    @PostMapping("/guardar")
    public String guardarVideojuego(@ModelAttribute("videojuego") Videojuego videojuego) {
        videojuegoRepository.save(videojuego);
        return "redirect:/videojuegos";
    }

    // Update: Cargar datos para editar
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Videojuego videojuego = videojuegoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de videojuego inválido: " + id));
        model.addAttribute("videojuego", videojuego);
        model.addAttribute("listaCategorias", categoriaRepository.findAll());
        return "videojuego_form";
    }

    // Delete: Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarVideojuego(@PathVariable Long id) {
        videojuegoRepository.deleteById(id);
        return "redirect:/videojuegos";
    }
    
 // Read: Mostrar Ficha de Detalles de un Juego
    @GetMapping("/detalle/{id}")
    public String verDetalles(@PathVariable Long id, Model model) {
        Videojuego videojuego = videojuegoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de videojuego inválido: " + id));
        model.addAttribute("juego", videojuego);
        return "videojuego_detalle";
    }
	
}
