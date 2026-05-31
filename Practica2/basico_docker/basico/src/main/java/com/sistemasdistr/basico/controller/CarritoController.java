package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.model.Videojuego;
import com.sistemasdistr.basico.repository.VideojuegoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final VideojuegoRepository videojuegoRepository;

    public CarritoController(VideojuegoRepository videojuegoRepository) {
        this.videojuegoRepository = videojuegoRepository;
    }

    // Método auxiliar para obtener (o crear) el carrito de la sesión actual
    @SuppressWarnings("unchecked")
    private List<Videojuego> obtenerCarrito(HttpSession session) {
        List<Videojuego> carrito = (List<Videojuego>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    // 1. Ver la pantalla del carrito
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        List<Videojuego> carrito = obtenerCarrito(session);
        double total = carrito.stream().mapToDouble(Videojuego::getPrecio).sum();
        
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "carrito"; // Muestra carrito.html
    }

    // 2. Añadir un juego al carrito
    @GetMapping("/add/{id}")
    public String agregarAlCarrito(@PathVariable Long id, HttpSession session) {
        Videojuego juego = videojuegoRepository.findById(id).orElseThrow();
        List<Videojuego> carrito = obtenerCarrito(session);
        
        // Solo añadimos si hay stock
        if (juego.getStock() > 0) {
            carrito.add(juego);
        }
        return "redirect:/videojuegos"; // Redirige al catálogo tras añadir
    }

    // 3. Eliminar un juego del carrito (por su posición en la lista)
    @GetMapping("/remove/{indice}")
    public String eliminarDelCarrito(@PathVariable int indice, HttpSession session) {
        List<Videojuego> carrito = obtenerCarrito(session);
        if (indice >= 0 && indice < carrito.size()) {
            carrito.remove(indice);
        }
        return "redirect:/carrito";
    }
}