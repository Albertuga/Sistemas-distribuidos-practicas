package com.sistemasdistr.basico.controller;

import com.sistemasdistr.basico.service.PythonApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/simular")
public class ApiController {

    private final PythonApiService pythonApiService;

    public ApiController(PythonApiService pythonApiService) {
        this.pythonApiService = pythonApiService;
    }

    @GetMapping("/archivo")
    public String testArchivo(ModelMap model) {
        String resultado = pythonApiService.testFileException();
        model.addAttribute("resultado", resultado);
        return "exito_api";
    }

    @GetMapping("/bd")
    public String testBaseDatos(ModelMap model) {
        String resultado = pythonApiService.testDbException();
        model.addAttribute("resultado", resultado);
        return "exito_api";
    }

    @GetMapping("/pokemon")
    public String testPokemon(ModelMap model) {
        String resultado = pythonApiService.testPokemonException();
        model.addAttribute("resultado", resultado);
        return "exito_api";
    }
}