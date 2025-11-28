/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author User
 */
public class Accion {

    private String tipoAccion;            // agregar, eliminar, atender
    private Cliente clienteSnapshot;      // copia exacta del estado del cliente
    private LocalDateTime fechaHora;      // momento de la acción
    private String lugarOriginal;         // "cola" o "atendidos" (solo para eliminar)

    // Constructor general (acciones normales)
    public Accion(String tipoAccion, Cliente clienteSnapshot) {
        this.tipoAccion = tipoAccion.toLowerCase();
        this.clienteSnapshot = clienteSnapshot.copia(); // asegura snapshot perfecto
        this.fechaHora = LocalDateTime.now();
        this.lugarOriginal = ""; // por defecto
    }

    // Constructor especial para acciones que requieren saber de dónde salió (ELIMINAR)
    public Accion(String tipoAccion, Cliente clienteSnapshot, String lugarOriginal) {
        this.tipoAccion = tipoAccion.toLowerCase();
        this.clienteSnapshot = clienteSnapshot.copia();
        this.fechaHora = LocalDateTime.now();
        this.lugarOriginal = lugarOriginal.toLowerCase();
    }

    // Getters
    public String getTipoAccion() {
        return tipoAccion;
    }

    public Cliente getClienteSnapshot() {
        return clienteSnapshot;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getLugarOriginal() {
        return lugarOriginal;
    }
}
