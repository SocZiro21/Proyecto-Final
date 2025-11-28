/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 *
 * @author User
 */
public class ColaClientes {
 
private Deque<Cliente> cola;
    private int contadorNormales = 0; // Regla: 2 normales → 1 urgente

    public ColaClientes() {
        cola = new ArrayDeque<>();
    }

    // ============================
    //        OPERACIONES BÁSICAS
    // ============================

    public void agregar(Cliente c) {
        cola.addLast(c);
    }

    public void agregarAlFrente(Cliente c) {
        cola.addFirst(c);
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public int tamaño() {
        return cola.size();
    }

    public List<Cliente> getCola() {
        return new LinkedList<>(cola);
    }

    // ============================
    //          BÚSQUEDAS
    // ============================

    public Cliente buscarPorId(String id) {
        for (Cliente c : cola) {
            if (c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    public List<Cliente> buscarPorTipo(String tipo) {
        List<Cliente> out = new LinkedList<>();
        for (Cliente c : cola) {
            if (c.getTipoSolicitud().equalsIgnoreCase(tipo)) {
                out.add(c);
            }
        }
        return out;
    }

    // ============================
    //        ELIMINACIONES
    // ============================

    public boolean eliminarPorId(String id) {
        return cola.removeIf(c -> c.getId().equalsIgnoreCase(id));
    }

    public Cliente eliminarPrimero() {
        return cola.pollFirst();
    }

    // ============================================================
    //      🔥 Insertar según horaLlegada (Orden cronológico real)
    // ============================================================

    public void insertarOrdenadoPorHora(Cliente cliente) {

        if (cola.isEmpty()) {
            cola.add(cliente);
            return;
        }

        ArrayDeque<Cliente> nuevaCola = new ArrayDeque<>();
        boolean insertado = false;

        for (Cliente c : cola) {
            if (!insertado && cliente.getHoraLlegada().isBefore(c.getHoraLlegada())) {
                nuevaCola.add(cliente);
                insertado = true;
            }
            nuevaCola.add(c);
        }

        if (!insertado) {
            nuevaCola.add(cliente);
        }

        cola = nuevaCola;
    }

    // ============================================================
    //      🔥 MÉTODOS PARA ATENCIÓN (del controlador)
    // ============================================================

    public Cliente obtenerPrimeroUrgente() {
        for (Cliente c : cola) {
            if (c.getPrioridad().equalsIgnoreCase("Urgente")) {
                return c;
            }
        }
        return null;
    }

    public Cliente atenderUrgente(Cliente urgente) {
        if (urgente == null) return null;

        boolean removed = cola.remove(urgente);

        if (removed) {
            contadorNormales = 0; // Reiniciar ciclo después de atender urgente
            return urgente;
        }
        return null;
    }

    // ============================================================
    //            🔥 LÓGICA PRINCIPAL DE ATENCIÓN
    // ============================================================

    public Cliente atender() {

        if (cola.isEmpty()) return null;

        Cliente urgente = obtenerPrimeroUrgente();

        // ========= 1. Antes de 2 normales → buscar normal primero =========
        if (contadorNormales < 2) {
            for (Cliente c : cola) {
                if (!c.getPrioridad().equalsIgnoreCase("Urgente")) {
                    cola.remove(c);
                    contadorNormales++;
                    return c;
                }
            }

            // No hay normales → atender urgente
            if (urgente != null) {
                cola.remove(urgente);
                contadorNormales = 0;
                return urgente;
            }
        }

        // ========= 2. Ya van 2 normales → toca urgente aunque llegue después =========
        if (urgente != null) {
            cola.remove(urgente);
            contadorNormales = 0;
            return urgente;
        }

        // ========= 3. No hay urgentes → atender el primero normal =========
        Cliente primero = cola.pollFirst();
        contadorNormales++;
        return primero;
    }
}
