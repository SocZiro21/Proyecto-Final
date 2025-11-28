/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package App;

import Controlador.Controlador;
import Modelo.ColaClientes;
import Modelo.ListaAtendidos;
import Vista.Vista;
import javax.swing.SwingUtilities;

/**
 *
 * @author User
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
     
        // Ejecutar en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            // Crear la vista
            Vista vista = new Vista();
            
            // Hacer visible la ventana
            vista.setVisible(true);
            
            // Crear el controlador con la vista
            Controlador controlador = new Controlador(vista);
            vista.setLocationRelativeTo(null);
        });
        
    }
    
}
