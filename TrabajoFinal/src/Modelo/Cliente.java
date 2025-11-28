/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author User
 */
public class Cliente {  
  private String id;
    private String nombre;
    private String tipoSolicitud; // Soporte, Mantenimiento, Reclamo
    private String prioridad;     // Normal, Urgente

    private LocalDateTime horaLlegada;
    private LocalDateTime horaSalida; // null hasta ser atendido
    private int tiempoAtencion; // minutos, 0 hasta ser atendido

    // ==========================================
    // CONSTRUCTOR NORMAL (NUEVO REGISTRO)
    // ==========================================
    public Cliente(String id, String nombre, String tipoSolicitud, String prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.tipoSolicitud = tipoSolicitud;
        this.prioridad = prioridad;
        this.horaLlegada = LocalDateTime.now();   // → HORA REAL DE LLEGADA
        this.horaSalida = null;
        this.tiempoAtencion = 0;
    }

    // ==========================================
    // CONSTRUCTOR COPIA (CLON REAL)
    // ==========================================
    public Cliente(String id, String nombre, String tipoSolicitud, String prioridad,
                   LocalDateTime horaLlegada, LocalDateTime horaSalida, int tiempoAtencion) {

        this.id = id;
        this.nombre = nombre;
        this.tipoSolicitud = tipoSolicitud;
        this.prioridad = prioridad;

        // 👇 Se garantiza que conserva EXACTAMENTE los valores
        this.horaLlegada = horaLlegada;
        this.horaSalida = horaSalida;
        this.tiempoAtencion = tiempoAtencion;
    }

    // ==========================================
    // GETTERS
    // ==========================================
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipoSolicitud() { return tipoSolicitud; }
    public String getPrioridad() { return prioridad; }
    public LocalDateTime getHoraLlegada() { return horaLlegada; }
    public LocalDateTime getHoraSalida() { return horaSalida; }
    public int getTiempoAtencion() { return tiempoAtencion; }

    // ==========================================
    // SETTERS
    // ==========================================
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }
    public void setTiempoAtencion(int tiempoAtencion) { this.tiempoAtencion = tiempoAtencion; }

    // ==========================================
    // FORMATO FECHAS
    // ==========================================
    public String formato(LocalDateTime ldt) {
        if (ldt == null) return "-";
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return ldt.format(f);
    }

    // ==========================================
    // TO STRING (para debug)
    // ==========================================
    @Override
    public String toString() {
        return "[" + id + "] " + nombre + " | " + tipoSolicitud + " | " + prioridad +
               " | Entrada: " + formato(horaLlegada) +
               " | Salida: " + formato(horaSalida) +
               " | Tiempo: " + tiempoAtencion + " min";
    }

    // ==========================================
    // COPIA PROFUNDA (CLON PERFECTO)
    // ==========================================
    public Cliente copia() {
        return new Cliente(
            this.id,
            this.nombre,
            this.tipoSolicitud,
            this.prioridad,
            this.horaLlegada,     // 👈 SE CONSERVA HORA DE LLEGADA ORIGINAL
            this.horaSalida,
            this.tiempoAtencion
        );
    }
}
