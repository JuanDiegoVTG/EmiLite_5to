package com.proyecto.emilite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Importante: usar @Controller, no @RestController
public class DashboardController {

    @GetMapping("/dashboard") // La URL debe coincidir con la de defaultSuccessUrl
    public String dashboard() {
        return "dashboard"; // Devuelve el nombre del archivo HTML sin extensión
    }
}