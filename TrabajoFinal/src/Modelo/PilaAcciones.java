/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author User
 */
public class PilaAcciones {
 private final Stack<Accion> pila;

    public PilaAcciones() {
        pila = new Stack<>();
    }

    public void apilar(Accion a) { pila.push(a); }

    public Accion desapilar() { return pila.isEmpty() ? null : pila.pop(); }

    public boolean estaVacia() { return pila.isEmpty(); }

    public List<Accion> obtenerTrazaInversa() {
        List<Accion> out = new ArrayList<>();
        for (int i = pila.size() - 1; i >= 0; i--) out.add(pila.get(i));
        return out;
    }
}
