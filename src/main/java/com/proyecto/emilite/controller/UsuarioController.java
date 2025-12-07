package com.proyecto.emilite.controller;

import com.proyecto.emilite.model.Rol; 
import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.model.dto.UsuarioRegistroDTO; 
import com.proyecto.emilite.service.UsuarioService;
import com.proyecto.emilite.service.RolService; 
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService; 

    
    //  Mostrar el formulario para que un usuario (cliente o admin) se registre
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuarioForm", new UsuarioRegistroDTO()); 
        // Cargar roles desde el RolService
        List<Rol> roles = rolService.findAll(); 
        model.addAttribute("roles", roles);
        return "registro_usuario"; 
    }

   //Procesar el formulario de creación de un nuevo usuario
    @PostMapping("/crear")
    public String crearUsuario(@Valid @ModelAttribute("usuarioForm") UsuarioRegistroDTO usuarioForm,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
           
            model.addAttribute("roles", rolService.findAll()); // 
            return "registro_usuario";
        }

        try {
            // Si no hay errores de validación, intenta crear el usuario
            usuarioService.crearUsuarioDesdeDTO(usuarioForm); // Llama al servicio para crear el usuario
            // Si la creación es exitosa, redirige a la página de login con un mensaje de éxito
            return "redirect:/login?registrado=success";
        } catch (Exception e) {
            
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            // Recarga la lista de roles para el formulario
            model.addAttribute("roles", rolService.findAll()); 
            // Vuelve al formulario de registro con el mensaje de error
            return "registro_usuario";
        }
    }

    // Endpoint: GET /usuarios (solo para ADMIN)
    // Propósito: Mostrar la lista de usuarios (solo para ADMIN)
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "lista_usuarios"; // Vista para la lista de usuarios (debes crearla)
    }

     // Propósito: Mostrar el formulario de registro para cualquier usuario no logueado
    @GetMapping("/registro-publico") // <-- Nueva ruta pública
    public String mostrarFormularioRegistroPublico(Model model) {
        model.addAttribute("usuarioForm", new UsuarioRegistroDTO()); // Objeto vacío para el formulario

       
       
        List<Rol> rolesCliente = rolService.findByNombre("CLIENTE");
        if (rolesCliente.isEmpty()) {
            
            model.addAttribute("error", "No se puede registrar en este momento. Contacte al administrador.");
            return "error"; 
        }
        model.addAttribute("roles", rolesCliente); 
        return "registro_publico"; 
    }


    // Procesar el formulario de registro público
    @PostMapping("/crear-publico") 
    public String crearUsuarioPublico(@Valid @ModelAttribute("usuarioForm") UsuarioRegistroDTO usuarioForm,
                                      BindingResult result,
                                      Model model) {
        if (result.hasErrors()) {
            // Si hay errores de validación, vuelve al formulario con los errores
            List<Rol> rolesCliente = rolService.findByNombre("CLIENTE");
            model.addAttribute("roles", rolesCliente);
            return "registro_publico";
        }

        // Verificar si el nombre de usuario ya existe
        if (usuarioService.findByUserName(usuarioForm.getUserName()).isPresent()) {
            result.rejectValue("userName", "error.usuarioForm", "El nombre de usuario ya está en uso.");
            List<Rol> rolesCliente = rolService.findByNombre("CLIENTE");
            model.addAttribute("roles", rolesCliente);
            return "registro_publico";
        }

        // Verificar si el email ya existe (opcional, si tienes email único)
        if (usuarioForm.getEmail() != null && !usuarioForm.getEmail().isEmpty() &&
            usuarioService.findByEmail(usuarioForm.getEmail()).isPresent()) { // Asumiendo que tienes este método
            result.rejectValue("email", "error.usuarioForm", "El email ya está en uso.");
            List<Rol> rolesCliente = rolService.findByNombre("CLIENTE");
            model.addAttribute("roles", rolesCliente);
            return "registro_publico";
        }

        try {
            // Asignar el rol CLIENTE por defecto
           
            Rol rolCliente = rolService.findByNombre("CLIENTE") 
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

            // Asignar el ID del rol al DTO antes de pasarlo al servicio
            usuarioForm.setRolId(rolCliente.getId());

            // Llamar al servicio para crear el usuario
            usuarioService.crearUsuarioDesdeDTO(usuarioForm);

            // Redirigir a la página de login con un mensaje de éxito
            return "redirect:/login?registrado=success";

        } catch (Exception e) {
            // Manejo de errores inesperados
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            List<Rol> rolesCliente = rolService.findByNombre("CLIENTE");
            model.addAttribute("roles", rolesCliente);
            return "registro_publico";
        }
    }

}
