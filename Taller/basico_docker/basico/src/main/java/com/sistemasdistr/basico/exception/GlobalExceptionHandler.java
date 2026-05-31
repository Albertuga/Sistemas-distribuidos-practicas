package com.sistemasdistr.basico.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PythonApiException.class)
    public String handlePythonApiException(PythonApiException ex, Model model) {
        String translatedMessage = translateError(ex.getErrorCode());
        
        // Pasamos la información a la vista (Thymeleaf)
        model.addAttribute("mensajeTraducido", translatedMessage);
        model.addAttribute("detalleTecnico", ex.getMessage());
        
        // Retornamos el nombre de la plantilla HTML que mostrará el error
        return "error_api"; 
    }

    // Método para traducir el código de error a español
    private String translateError(String errorCode) {
        return switch (errorCode) {
            case "FILE_ERROR" -> "No se ha podido localizar o leer el archivo de configuración en el servidor remoto.";
            case "DB_ERROR" -> "Hubo un problema de conexión con la base de datos externa. Por favor, inténtalo más tarde.";
            case "API_THIRD_PARTY_ERROR" -> "Falló la comunicación con el servicio externo de Pokémon.";
            default -> "Ha ocurrido un error inesperado en el sistema.";
        };
    }
}