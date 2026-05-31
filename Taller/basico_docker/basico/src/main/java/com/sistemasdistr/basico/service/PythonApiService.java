package com.sistemasdistr.basico.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemasdistr.basico.exception.PythonApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class PythonApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PythonApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    private String callApi(String endpoint) {
        // La URL apunta al nombre del servicio en docker-compose (python-api)
        String url = "http://python-api:5000/api/test/" + endpoint;
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (HttpStatusCodeException e) {
            // Si la API en Python falla (Devuelve 500), capturamos el JSON del error
            try {
                JsonNode root = objectMapper.readTree(e.getResponseBodyAsString());
                String errorCode = root.path("error").asText();
                String details = root.path("details").asText();
                
                // Lanzamos nuestra propia excepción
                throw new PythonApiException(errorCode, details);
            } catch (Exception ex) {
                throw new RuntimeException("Error inesperado al parsear el error de la API", ex);
            }
        }
    }

    public String testFileException() {
        return callApi("file");
    }

    public String testDbException() {
        return callApi("db");
    }

    public String testPokemonException() {
        return callApi("pokemon");
    }
}