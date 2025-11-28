/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.util.List;
/**
 *
 * @author User
 */
public class ModeloHelper {
   
     // 🔥 VALIDAR QUE NO SE REPITA EL ID
    public static boolean idExiste(String id, ColaClientes cola, ListaAtendidos lista) {

        // Buscar en la cola
        if (cola.buscarPorId(id) != null) {
            return true;
        }

        // Buscar en la lista de atendidos
        if (lista.buscarPorId(id) != null) {
            return true;
        }

        return false;
    }

    // 🔥 VALIDAR NOMBRE SOLO LETRAS
    public static boolean nombreValido(String nombre) {
        return nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
    }

    // 🔥 VALIDAR ID SOLO NÚMEROS
    public static boolean idValido(String id) {
        return id.matches("^[0-9]+$");
    }
}
