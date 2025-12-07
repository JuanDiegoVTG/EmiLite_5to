package com.proyecto.emilite.controller;

import com.proyecto.emilite.model.Rutina;
import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.model.dto.RutinaFormDTO; // Asumiendo que creas un DTO
import com.proyecto.emilite.service.RutinaService;
import com.proyecto.emilite.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EntrenadorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RutinaService rutinaService;

    // Endpoint: GET /entrenador/clientes
    // Propósito: Mostrar los clientes del sistema (accesible por ENTRENADOR)
    @GetMapping("/entrenador/clientes")
    public String verClientes(Model model) {
        // Obtener todos los usuarios con rol 'CLIENTE'
        List<Usuario> clientes = usuarioService.findByRolNombre("CLIENTE"); // Asumiendo que tienes este método en UsuarioService

        model.addAttribute("clientes", clientes);
        return "entrenador/mis_clientes"; // Vista para los clientes
    }

     // Propósito: Mostrar el formulario para crear una nueva rutina (accesible por ENTRENADOR)
    @GetMapping("/entrenador/rutinas/nueva")
    public String mostrarFormularioCreacionRutina(Model model) {
        model.addAttribute("rutinaForm", new RutinaFormDTO()); // Objeto vacío para el formulario
        // Cargar listas para los selects (clientes)
        model.addAttribute("clientes", usuarioService.findByRolNombre("CLIENTE")); // Solo clientes
        return "entrenador/form_rutina"; // Vista para el formulario de creación de rutina
    }

    // Endpoint: POST /entrenador/rutinas
    // Procesar el formulario de creación de una nueva rutina (accesible por ENTRENADOR)
    @PostMapping("/entrenador/rutinas")
    public String crearRutina(@Valid @ModelAttribute("rutinaForm") RutinaFormDTO rutinaForm,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            // Si hay errores de validación, vuelve al formulario con los errores
            model.addAttribute("clientes", usuarioService.findByRolNombre("CLIENTE"));
            return "entrenador/form_rutina";
        }

        // Crear la entidad Rutina desde el DTO
        Rutina nuevaRutina = new Rutina();
        nuevaRutina.setNombre(rutinaForm.getNombre());
        nuevaRutina.setDescripcion(rutinaForm.getDescripcion());
        nuevaRutina.setNivelDificultad(rutinaForm.getNivelDificultad());
        nuevaRutina.setTipo(rutinaForm.getTipo());
        nuevaRutina.setDuracionSemanas(rutinaForm.getDuracionSemanas());

        // Asignar cliente
        Usuario cliente = usuarioService.findById(rutinaForm.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + rutinaForm.getClienteId()));
        nuevaRutina.setCliente(cliente);

        // Fecha de creación se asigna por defecto en la entidad

        ;
        // Guardar la rutina
        rutinaService.save(nuevaRutina);

        // Redirigir a la lista de rutinas o clientes después de crear
        return "redirect:/entrenador/clientes"; // O a donde quieras
    }

    @GetMapping("/entrenador/rutinas") 
    public String verRutinas(Model model) { 
    
    return "entrenador/lista_rutinas"; 
    }
}