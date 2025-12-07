package com.proyecto.emilite.controller;

import com.proyecto.emilite.model.Servicio;
import com.proyecto.emilite.model.dto.ServicioFormDTO;
import com.proyecto.emilite.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/servicios") 
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

   
    //  Mostrar la lista de servicios
    @GetMapping
    public String listarServicios(Model model) {
        List<Servicio> servicios = servicioService.findAll();
        model.addAttribute("servicios", servicios);
        return "servicios/lista_servicios"; 
    }

    
    //  Mostrar el formulario para crear un nuevo servicio
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("servicioForm", new ServicioFormDTO()); 
        return "servicios/form_servicio"; 
    }

   
    //Procesar el formulario de creación de un nuevo servicio
    @PostMapping
    public String crearServicio(@Valid @ModelAttribute("servicioForm") ServicioFormDTO servicioForm,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            // Si hay errores de validación, vuelve al formulario con los errores
            return "servicios/form_servicio";
        }

        // Crear la entidad Servicio desde el DTO
        Servicio nuevoServicio = new Servicio();
        nuevoServicio.setNombre(servicioForm.getNombre());
        nuevoServicio.setDescripcion(servicioForm.getDescripcion());
        nuevoServicio.setDuracionMinutos(servicioForm.getDuracionMinutos());
        nuevoServicio.setPrecio(servicioForm.getPrecio());
        // Asignar 'activo' a true por defecto al crear
        nuevoServicio.setActivo(true);

        // Guardar el servicio
        servicioService.save(nuevoServicio);

        // Redirigir a la lista de servicios después de crear
        return "redirect:/servicios";
    }

    
    // Mostrar el formulario para editar un servicio existente
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Servicio servicio = servicioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        // Convertir la entidad Servicio a DTO para el formulario
        ServicioFormDTO servicioForm = new ServicioFormDTO();
        servicioForm.setNombre(servicio.getNombre());
        servicioForm.setDescripcion(servicio.getDescripcion());
        servicioForm.setDuracionMinutos(servicio.getDuracionMinutos());
        servicioForm.setPrecio(servicio.getPrecio());
    

        model.addAttribute("servicioForm", servicioForm);
        model.addAttribute("servicioId", id); 
        return "servicios/form_servicio"; 
    }

   
    // Procesar el formulario de edición de un servicio existente
    @PostMapping("/{id}")
    public String actualizarServicio(@PathVariable Long id,
                                     @Valid @ModelAttribute("servicioForm") ServicioFormDTO servicioForm,
                                     BindingResult result,
                                     Model model) {
        if (result.hasErrors()) {
            model.addAttribute("servicioId", id);
            // Si hay errores de validación, vuelve al formulario con los errores
            return "servicios/form_servicio";
        }

        Servicio servicioExistente = servicioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        // Actualizar la entidad con los datos del DTO
        servicioExistente.setNombre(servicioForm.getNombre());
        servicioExistente.setDescripcion(servicioForm.getDescripcion());
        servicioExistente.setDuracionMinutos(servicioForm.getDuracionMinutos());
        servicioExistente.setPrecio(servicioForm.getPrecio());
        // Puedes actualizar 'activo' si lo manejas en el form de edición

        // Guardar el servicio actualizado
        servicioService.save(servicioExistente);

        // Redirigir a la lista de servicios después de actualizar
        return "redirect:/servicios";
    }

    
    // Eliminar un servicio existente
    @PostMapping("/{id}/eliminar")
    public String eliminarServicio(@PathVariable Long id) {
        servicioService.deleteById(id);
        // Redirigir a la lista de servicios después de eliminar
        return "redirect:/servicios";
    }

    @GetMapping("/cliente/servicios")
    public String verServiciosCliente(Model model) {
    // Obtener todos los servicios activos
    List<Servicio> servicios = servicioService.findByActivo(true); 
    model.addAttribute("servicios", servicios);
    return "cliente/ver_servicios"; // Vista para mostrar los servicios al cliente
    }
}