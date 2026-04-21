package com.sistemasdistr.basico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Maincontroller {
    
    @GetMapping("/")
    public String vistaHome(ModelMap interfazConPantalla){
        return "index";
    }

    // ¡ESTE ES EL MÉTODO CLAVE PARA QUE SE VEA EL LOGIN!
    @GetMapping("/login")
    public String vistaLogin() {
        return "login"; // Esto le dice a Spring que busque el archivo login.html
    }
}