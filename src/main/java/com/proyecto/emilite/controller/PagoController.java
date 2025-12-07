package com.proyecto.emilite.controller;

import com.proyecto.emilite.model.Pago;
import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.model.Servicio;
import com.proyecto.emilite.model.dto.PagoFormDTO;
import com.proyecto.emilite.service.PagoService;
import com.proyecto.emilite.service.UsuarioService;
import com.proyecto.emilite.service.ServicioService;
import net.sf.jasperreports.engine.JRException; // Asegúrate de tener este import


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pagos") // Path base para operaciones de ADMIN
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicioService servicioService;


    // --- PARTE DEL ADMIN ---


    // Propósito: Mostrar la lista de pagos (solo para ADMIN)
    @GetMapping
    public String listarPagos(Model model) {
        List<Pago> pagos = pagoService.findAll();
        model.addAttribute("pagos", pagos);
        return "pagos/lista_pagos"; // Vista para la lista (debes crearla)
    }

    
    // Propósito: Mostrar el formulario para crear un nuevo pago (solo para ADMIN)
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("pagoForm", new PagoFormDTO()); 
        // Cargar listas para los selects (usuarios, servicios, promociones)
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("servicios", servicioService.findAll());

        return "pagos/form_pago"; 
    }

    
    // Procesar el formulario de creación de un nuevo pago (solo para ADMIN)
    @PostMapping
    public String crearPago(@Valid @ModelAttribute("pagoForm") PagoFormDTO pagoForm,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            // Si hay errores de validación, vuelve al formulario con los errores
            // Recargar listas para los selects
            model.addAttribute("usuarios", usuarioService.findAll());
            model.addAttribute("servicios", servicioService.findAll());
        
            return "pagos/form_pago";
        }

        // Crear la entidad Pago desde el DTO
        Pago nuevoPago = new Pago();
        // Asignar usuario
        Usuario usuario = usuarioService.findById(pagoForm.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + pagoForm.getUsuarioId()));
        nuevoPago.setUsuario(usuario);

        // Asignar servicio (si se proporcionó)
        if (pagoForm.getServicioId() != null) {
            Servicio servicio = servicioService.findById(pagoForm.getServicioId())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + pagoForm.getServicioId()));
            nuevoPago.setServicio(servicio);
        }

       

        nuevoPago.setMonto(pagoForm.getMonto());
        nuevoPago.setMetodoPago(pagoForm.getMetodoPago());
        nuevoPago.setEstado(pagoForm.getEstado());
        nuevoPago.setReferenciaPago(pagoForm.getReferenciaPago());
        // Fecha de pago se asigna por defecto en la entidad

        // Guardar el pago
        pagoService.save(nuevoPago);

        // Redirigir a la lista de pagos después de crear
        return "redirect:/pagos";
    }

    
    // Mostrar el formulario para editar un pago existente (solo para ADMIN)
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Pago pago = pagoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        // Convertir la entidad Pago a DTO para el formulario
        PagoFormDTO pagoForm = new PagoFormDTO();
        pagoForm.setUsuarioId(pago.getUsuario().getId());
        pagoForm.setServicioId(pago.getServicio() != null ? pago.getServicio().getId() : null);
        pagoForm.setMonto(pago.getMonto());
        pagoForm.setMetodoPago(pago.getMetodoPago());
        pagoForm.setEstado(pago.getEstado());
        pagoForm.setReferenciaPago(pago.getReferenciaPago());

        model.addAttribute("pagoForm", pagoForm);
        model.addAttribute("pagoId", id); 
        // Cargar listas para los selects (usuarios, servicios, promociones)
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("servicios", servicioService.findAll());
        return "pagos/form_pago"; // Vista para el formulario de edición
    }

    
    // Procesar el formulario de edición de un pago existente (solo para ADMIN)
    @PostMapping("/{id}")
    public String actualizarPago(@PathVariable Long id,
                                 @Valid @ModelAttribute("pagoForm") PagoFormDTO pagoForm,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pagoId", id);
            // Recargar listas para los selects
            model.addAttribute("usuarios", usuarioService.findAll());
            model.addAttribute("servicios", servicioService.findAll());
            // Si hay errores de validación, vuelve al formulario con los errores
            return "pagos/form_pago";
        }

        Pago pagoExistente = pagoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        // Actualizar la entidad con los datos del DTO
        // Asignar usuario
        Usuario usuario = usuarioService.findById(pagoForm.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + pagoForm.getUsuarioId()));
        pagoExistente.setUsuario(usuario);

        // Asignar servicio (si se proporcionó)
        if (pagoForm.getServicioId() != null) {
            Servicio servicio = servicioService.findById(pagoForm.getServicioId())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + pagoForm.getServicioId()));
            pagoExistente.setServicio(servicio);
        } else {
            pagoExistente.setServicio(null); // Limpiar si se dejó en blanco
        }

        
        pagoExistente.setMonto(pagoForm.getMonto());
        pagoExistente.setMetodoPago(pagoForm.getMetodoPago());
        pagoExistente.setEstado(pagoForm.getEstado());
        pagoExistente.setReferenciaPago(pagoForm.getReferenciaPago());

        // Guardar el pago actualizado
        pagoService.save(pagoExistente);

        // Redirigir a la lista de pagos después de actualizar
        return "redirect:/pagos";
    }


    // Eliminar un pago existente (solo para ADMIN)
    @PostMapping("/{id}/eliminar")
    public String eliminarPago(@PathVariable Long id) {
        pagoService.deleteById(id);
        // Redirigir a la lista de pagos después de eliminar
        return "redirect:/pagos";
    }

    // --- PARTE DEL CLIENTE ---

    //
    //  Mostrar los pagos del cliente logueado
    @GetMapping("/cliente") 
    public String verPagosCliente(Model model) {
        // Obtener el nombre de usuario del usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Obtener la entidad Usuario desde la base de datos
        Usuario usuarioLogueado = usuarioService.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        // Obtener los pagos asociados a este usuario
        List<Pago> pagosDelCliente = pagoService.findByUsuarioId(usuarioLogueado.getId());

        // Añadir la lista de pagos al modelo para que la vista pueda mostrarla
        model.addAttribute("pagos", pagosDelCliente);

        // Devolver la vista específica para los pagos del cliente
        return "cliente/mis_pagos"; // Vista para los pagos del cliente (debes crearla)
    }

   
}