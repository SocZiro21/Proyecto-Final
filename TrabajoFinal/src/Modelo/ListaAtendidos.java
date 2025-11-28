/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author User
 */
public class ListaAtendidos {    

   private LinkedList<Cliente> lista;

    public ListaAtendidos() {
        lista = new LinkedList<>();
    }

    // Agregar un cliente atendido
    public void agregarAtendido(Cliente c) {
        lista.addLast(c);
    }

    // Eliminar cliente por ID
    public boolean eliminarPorId(String id) {
        for (int i = lista.size() - 1; i >= 0; i--) {
            if (lista.get(i).getId().equalsIgnoreCase(id)) {
                lista.remove(i);
                return true;
            }
        }
        return false;
    }

    // Obtener lista completa
    public List<Cliente> getLista() {
        return lista;
    }

    // Cuántos atendidos hay
    public int totalAtendidos() {
        return lista.size();
    }

    // Promedio del tiempo de atención
    public double promedioTiempoAtencion() {
        if (lista.isEmpty()) return 0.0;
        double suma = 0;
        for (Cliente c : lista) {
            suma += c.getTiempoAtencion();
        }
        return suma / lista.size();
    }

    // 🔥 BUSCAR CLIENTE POR ID (lo pide el Controlador)
    public Cliente buscarPorId(String id) {
        for (Cliente c : lista) {
            if (c.getId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    // 🔥 BUSCAR CLIENTES POR TIPO (lo pide el Controlador)
    public List<Cliente> buscarPorTipo(String tipo) {
        List<Cliente> result = new LinkedList<>();
        for (Cliente c : lista) {
            if (c.getTipoSolicitud().equalsIgnoreCase(tipo)) {
                result.add(c);
            }
        }
        return result;
    }
}
