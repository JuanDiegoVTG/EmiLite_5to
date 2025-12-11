package com.proyecto.emilite.controller;

import com.proyecto.emilite.model.Rutina;
import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.model.dto.RutinaFormDTO;
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

    // Mostrar clientes del entrenador
    @GetMapping("/entrenador/clientes")
    public String verClientes(Model model) {
        List<Usuario> clientes = usuarioService.findByRolNombre("CLIENTE");
        model.addAttribute("clientes", clientes);
        return "entrenador/mis_clientes";
    }

    // Formulario para nueva rutina
    @GetMapping("/entrenador/rutinas/nueva")
    public String mostrarFormularioCreacionRutina(Model model) {
        model.addAttribute("rutinaForm", new RutinaFormDTO());
        model.addAttribute("clientes", usuarioService.findByRolNombre("CLIENTE"));
        return "entrenador/form_rutina";
    }

    // Crear rutina
    @PostMapping("/entrenador/rutinas")
    public String crearRutina(@Valid @ModelAttribute("rutinaForm") RutinaFormDTO rutinaForm,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            model.addAttribute("clientes", usuarioService.findByRolNombre("CLIENTE"));
            return "entrenador/form_rutina";
        }

        Rutina nuevaRutina = new Rutina();
        nuevaRutina.setNombre(rutinaForm.getNombre());
        nuevaRutina.setDescripcion(rutinaForm.getDescripcion());
        nuevaRutina.setNivelDificultad(rutinaForm.getNivelDificultad());
        nuevaRutina.setTipo(rutinaForm.getTipo());
        nuevaRutina.setDuracionSemanas(rutinaForm.getDuracionSemanas());

        // Ya NO usamos Optional — UsuarioService.findById devuelve Usuario directo
        Usuario cliente = usuarioService.findById(rutinaForm.getClienteId());
        nuevaRutina.setCliente(cliente);

        rutinaService.save(nuevaRutina);

        return "redirect:/entrenador/clientes";
    }

    // Vista general de rutinas
    @GetMapping("/entrenador/rutinas")
    public String verRutinas(Model model) {
        // Si quieres mostrar rutinas, aquí agregas:
        // model.addAttribute("rutinas", rutinaService.findAll());
        return "entrenador/lista_rutinas";
    }
}
