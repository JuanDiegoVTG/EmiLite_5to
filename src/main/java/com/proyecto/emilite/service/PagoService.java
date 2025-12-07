package com.proyecto.emilite.service;

import com.proyecto.emilite.model.Pago;
import com.proyecto.emilite.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    // Método para obtener todos los pagos (solo ADMIN)
    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    // Método para obtener un pago por ID
    public Optional<Pago> findById(Long id) {
        return pagoRepository.findById(id);
    }

    // Método para guardar (crear o actualizar) un pago
    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    // Método para eliminar un pago por ID
    public void deleteById(Long id) {
        pagoRepository.deleteById(id);
    }

    // Método específico para encontrar pagos por cliente (clave para la vista del cliente)
    public List<Pago> findByUsuarioId(Long usuarioId) {
        return pagoRepository.findByUsuarioId(usuarioId);
    }
}