package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.model.Videojuego;
import com.sistemasdistr.basico.repository.VideojuegoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pago")
public class PagoController {

    private final VideojuegoRepository videojuegoRepository;

    public PagoController(VideojuegoRepository videojuegoRepository) {
        this.videojuegoRepository = videojuegoRepository;
    }

    // 1. Mostrar pasarela de pago para todo el carrito
    @GetMapping("/checkout")
    @SuppressWarnings("unchecked")
    public String pantallaPago(HttpSession session, Model model) {
        List<Videojuego> carrito = (List<Videojuego>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/videojuegos"; // Si el carrito está vacío, echa al usuario al catálogo
        }
        
        double total = carrito.stream().mapToDouble(Videojuego::getPrecio).sum();
        model.addAttribute("total", total);
        model.addAttribute("cantidadItems", carrito.size());
        return "pago"; 
    }

    // 2. Procesar la compra de todo el carrito
    @PostMapping("/procesar")
    @SuppressWarnings("unchecked")
    public String procesarPago(HttpSession session, Model model) {
        List<Videojuego> carrito = (List<Videojuego>) session.getAttribute("carrito");
        
        if (carrito != null && !carrito.isEmpty()) {
            double totalPagado = 0;
            
            // Restamos 1 de stock por cada juego en el carrito
            for (Videojuego juegoCarrito : carrito) {
                Videojuego juegoBD = videojuegoRepository.findById(juegoCarrito.getId()).orElse(null);
                if (juegoBD != null && juegoBD.getStock() > 0) {
                    juegoBD.setStock(juegoBD.getStock() - 1);
                    videojuegoRepository.save(juegoBD);
                    totalPagado += juegoBD.getPrecio();
                }
            }
            
            model.addAttribute("total", totalPagado);
            // ¡Vaciamos el carrito tras pagar!
            session.removeAttribute("carrito"); 
        }
        return "pago_exito"; 
    }
}