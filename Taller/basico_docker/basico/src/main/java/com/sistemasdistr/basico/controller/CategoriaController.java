package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.model.Categoria;
import com.sistemasdistr.basico.repository.CategoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * CategoriaController
 * Se necesita una clase java que atienda a las peticiones de usuario cuando 
 * cliquemos en categorias.
 * */

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
	
	private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

 // Read: Mostrar todas las categorías o filtrar por búsqueda
    @GetMapping
    public String listarCategorias(@RequestParam(value = "buscar", required = false) String buscar, Model model) {
        if (buscar != null && !buscar.isEmpty()) {
            model.addAttribute("categorias", categoriaRepository.findByNombreContainingIgnoreCase(buscar));
        } else {
            model.addAttribute("categorias", categoriaRepository.findAll());
        }
        model.addAttribute("buscar", buscar); // Pasamos el texto a la vista para mantenerlo en la barra
        return "categorias";
    }

    // Create: Mostrar el formulario en blanco
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria_form"; // Llama al archivo categoria_form.html
    }

    // Create/Update: Guardar los datos en la Base de Datos
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute("categoria") Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias"; // Vuelve a la lista
    }

    // Update: Mostrar el formulario con los datos cargados
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCategoria(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de categoría inválido: " + id));
        model.addAttribute("categoria", categoria);
        return "categoria_form";
    }

    // Delete: Borrar una categoría
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
        return "redirect:/categorias";
    }
    
}
