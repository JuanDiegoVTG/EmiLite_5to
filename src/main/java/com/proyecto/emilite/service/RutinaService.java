package com.proyecto.emilite.service;

import com.proyecto.emilite.model.Rutina;
import com.proyecto.emilite.repository.RutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;

    public List<Rutina> findAll() {
        return rutinaRepository.findAll();
    }

    public Optional<Rutina> findById(@NonNull Long id) {
        return rutinaRepository.findById(id);
    }

   // Método para guardar (crear o actualizar) una rutina
    public @NonNull Rutina save(@NonNull Rutina rutina) {
        return rutinaRepository.save(rutina); 
    }

    public void deleteById(@NonNull Long id) {
        rutinaRepository.deleteById(id);
    }
    

    // Método específico para encontrar rutinas por cliente
    public List<Rutina> findByClienteId(@NonNull Long clienteId) {
        return rutinaRepository.findByClienteId(clienteId);
    }
}