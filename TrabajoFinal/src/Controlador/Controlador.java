/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Accion;
import Modelo.Cliente;
import Modelo.ColaClientes;
import Modelo.ListaAtendidos;
import Modelo.PilaAcciones;
import Modelo.ModeloHelper;
import Vista.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class Controlador implements ActionListener {
private final Vista vista;
    private final ColaClientes cola;
    private final ListaAtendidos lista;
    private final PilaAcciones pila;

    private int contadorNormales = 0;

    public Controlador(Vista vista) {
        this.vista = vista;
        this.cola = new ColaClientes();
        this.lista = new ListaAtendidos();
        this.pila = new PilaAcciones();

        vista.getBtnAgregar().addActionListener(e -> agregarCliente());
        vista.getBtnAtender().addActionListener(e -> atenderCliente());
        vista.getBtnEliminar().addActionListener(e -> eliminarCliente());
        vista.getBtnDeshacer().addActionListener(e -> deshacerAccion());
        vista.getBtnMostrarHistorial().addActionListener(e -> mostrarHistorialEnTabla());
        vista.getBtnMostrarEstadisticas().addActionListener(e -> mostrarEstadisticasEnTabla());
        vista.getBtnReiniciar().addActionListener(e -> reiniciarDia());

        try { vista.getBtnSalir1().addActionListener(e -> System.exit(0)); } catch (Exception ignored) {}
        try { vista.getBtnSalir2().addActionListener(e -> System.exit(0)); } catch (Exception ignored) {}
        try { vista.getBtnSalir3().addActionListener(e -> System.exit(0)); } catch (Exception ignored) {}

        vista.getBtnBuscarId().addActionListener(e -> buscarPorId());
        vista.getBtnBuscarSoporte().addActionListener(e -> buscarPorTipo("Soporte"));
        vista.getBtnBuscarMantenimiento().addActionListener(e -> buscarPorTipo("Mantenimiento"));
        vista.getBtnBuscarReclamo().addActionListener(e -> buscarPorTipo("Reclamo"));
        vista.getBtnMostrarTodos().addActionListener(e -> mostrarTodos());

        actualizarTablas();
    }

    // ==========================================================
    // VALIDACIONES
    // ==========================================================

    private boolean validarIdFormato(String id) {
        if (id == null || id.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El ID no puede estar vacío");
            return false;
        }
        if (!id.matches("\\d+")) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser numérico");
            return false;
        }
        return true;
    }

    private boolean validarNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre no puede estar vacío");
            return false;
        }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            JOptionPane.showMessageDialog(vista, "Nombre inválido");
            return false;
        }
        return true;
    }

    // ==========================================================
    // AGREGAR CLIENTE
    // ==========================================================

    private void agregarCliente() {
        String id = vista.getTxtId().getText().trim();
        String nombre = vista.getTxtNombre().getText().trim();
        String tipo = vista.getComboTipo().getSelectedItem().toString();
        String prioridad = vista.getComboPrioridad().getSelectedItem().toString();

        if (!validarIdFormato(id)) return;
        if (!validarNombre(nombre)) return;

        if (ModeloHelper.idExiste(id, cola, lista)) {
            JOptionPane.showMessageDialog(vista, "El ID ya existe.");
            return;
        }

        Cliente c = new Cliente(id, nombre, tipo, prioridad);
        cola.insertarOrdenadoPorHora(c);

        pila.apilar(new Accion("agregar", c));

        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        actualizarTablas();
    }

    // ==========================================================
    // ATENDER CLIENTE (2 normales → 1 urgente)
    // ==========================================================

    private void atenderCliente() {
       if (cola.estaVacia()) {
        JOptionPane.showMessageDialog(vista, "No hay clientes en la cola.");
        return;
    }

    Cliente seleccionado;

    Cliente urgente = cola.obtenerPrimeroUrgente();

    // ================================================
    // 1. Si ya atendí 2 normales → ahora toca urgente sí o sí
    // ================================================
    if (contadorNormales >= 2) {

        if (urgente != null) {
            seleccionado = cola.atenderUrgente(urgente);
            contadorNormales = 0; // reiniciar ciclo
        } else {
            // No hay urgentes → atender normal
            seleccionado = cola.atender();
            if (seleccionado.getPrioridad().equalsIgnoreCase("Normal")) {
                contadorNormales++;
            }
        }

    } else {

        // ================================================
        // 2. Todavía puedo atender normales → cola decide correctamente
        // ================================================
        seleccionado = cola.atender();

        if (seleccionado.getPrioridad().equalsIgnoreCase("Normal")) {
            contadorNormales++;
        } else {
            // si salió un urgente antes de cumplir los 2 normales, reiniciar ciclo
            contadorNormales = 0;
        }
    }

    // ================================================
    // 3. FINALIZAR ATENCIÓN
    // ================================================
    int tiempo = (int) (Math.random() * 6) + 5;
    seleccionado.setTiempoAtencion(tiempo);
    seleccionado.setHoraSalida(LocalDateTime.now());

    lista.agregarAtendido(seleccionado);
    pila.apilar(new Accion("atender", seleccionado));

    actualizarTablas();
    JOptionPane.showMessageDialog(vista, "Atendido: " + seleccionado.getNombre());
    }

    // ==========================================================
    // ELIMINAR
    // ==========================================================

    private void eliminarCliente() {
        String id = JOptionPane.showInputDialog(vista, "Ingrese el ID a eliminar:");

        if (id == null || id.trim().isEmpty()) return;

        Cliente cCola = cola.buscarPorId(id);
        Cliente cAtendido = lista.buscarPorId(id);

        if (cCola == null && cAtendido == null) {
            JOptionPane.showMessageDialog(vista, "No existe ese ID.");
            return;
        }

        if (cCola != null) {
            cola.eliminarPorId(id);
            pila.apilar(new Accion("eliminar", cCola, "cola"));
        } else {
            lista.eliminarPorId(id);
            pila.apilar(new Accion("eliminar", cAtendido, "atendidos"));
        }

        actualizarTablas();
        JOptionPane.showMessageDialog(vista, "Cliente eliminado: " + id);
    }

    // ==========================================================
    // DESHACER (REINSERTA ORDENADO POR HORA ORIGINAL)
    // ==========================================================

    private void deshacerAccion() {

        Accion a = pila.desapilar();
        if (a == null) {
            JOptionPane.showMessageDialog(vista, "No hay acciones para deshacer.");
            return;
        }

        Cliente snap = a.getClienteSnapshot().copia();
        Accion registro;

        switch (a.getTipoAccion()) {

            case "agregar":
                cola.eliminarPorId(snap.getId());
                registro = new Accion("deshacer agregar", snap.copia());
                break;

            case "eliminar":
                if (a.getLugarOriginal().equals("cola")) {
                    cola.insertarOrdenadoPorHora(snap.copia());
                    registro = new Accion("deshacer eliminar", snap.copia(), "cola");
                } else {
                    lista.agregarAtendido(snap.copia());
                    registro = new Accion("deshacer eliminar", snap.copia(), "atendidos");
                }
                break;

            case "atender":
                lista.eliminarPorId(snap.getId());

                snap.setHoraSalida(null);
                snap.setTiempoAtencion(0);

                cola.insertarOrdenadoPorHora(snap.copia());

                contadorNormales = 0;

                registro = new Accion("deshacer atender", snap.copia());
                break;

            default:
                return;
        }

        pila.apilar(registro);
        actualizarTablas();
    }

    // ==========================================================
    // HISTORIAL
    // ==========================================================

    private void mostrarHistorialEnTabla() {
        DefaultTableModel m = vista.getModelAcciones();
        m.setRowCount(0);

        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Accion a : pila.obtenerTrazaInversa()) {
            Cliente c = a.getClienteSnapshot();
            m.addRow(new Object[]{
                a.getTipoAccion(),
                c.getId(),
                c.getNombre(),
                a.getFechaHora().format(f)
            });
        }
    }

    // ==========================================================
    // ESTADÍSTICAS
    // ==========================================================

    private void mostrarEstadisticasEnTabla() {
        DefaultTableModel m = vista.getModelEstadisticas();
        m.setRowCount(0);

        m.addRow(new Object[]{"En espera", cola.tamaño()});
        m.addRow(new Object[]{"Atendidos", lista.totalAtendidos()});
        m.addRow(new Object[]{"Promedio atención", lista.promedioTiempoAtencion()});
    }

    // ==========================================================
    // REINICIAR DÍA
    // ==========================================================

    private void reiniciarDia() {
        int r = JOptionPane.showConfirmDialog(vista, "¿Reiniciar todo?");
        if (r != JOptionPane.YES_OPTION) return;

        lista.getLista().clear();
        vista.getModelCola().setRowCount(0);
        vista.getModelAtendidos().setRowCount(0);

        while (!cola.estaVacia()) cola.atender();
        while (!pila.estaVacia()) pila.desapilar();

        contadorNormales = 0;

        JOptionPane.showMessageDialog(vista, "Sistema reiniciado.");
        actualizarTablas();
    }

    // ==========================================================
    // BÚSQUEDAS
    // ==========================================================

    private void buscarPorId() {
        String id = vista.getTxtBuscarId().getText().trim();
    DefaultTableModel m = vista.getModelBusqueda();
    m.setRowCount(0);

    if (!validarIdFormato(id)) return;

    Cliente c1 = cola.buscarPorId(id);
    Cliente c2 = lista.buscarPorId(id);

    boolean encontrado = false;

    if (c1 != null) {
        agregarFilaBusqueda(m, c1);
        encontrado = true;
    }
    if (c2 != null) {
        agregarFilaBusqueda(m, c2);
        encontrado = true;
    }

    if (!encontrado) {
        JOptionPane.showMessageDialog(vista,
                "No se encontró ningún cliente con el ID: " + id,
                "Sin resultados",
                JOptionPane.WARNING_MESSAGE);
    }
    }

    private void buscarPorTipo(String tipo) {
       DefaultTableModel m = vista.getModelBusqueda();
    m.setRowCount(0);

    boolean encontrado = false;

    for (Cliente c : cola.buscarPorTipo(tipo)) {
        agregarFilaBusqueda(m, c);
        encontrado = true;
    }

    for (Cliente c : lista.buscarPorTipo(tipo)) {
        agregarFilaBusqueda(m, c);
        encontrado = true;
    }

    if (!encontrado) {
        JOptionPane.showMessageDialog(vista,
                "No hay clientes en el área de: " + tipo,
                "Sin resultados",
                JOptionPane.INFORMATION_MESSAGE);
    }
    }

    private void mostrarTodos() {
        DefaultTableModel m = vista.getModelBusqueda();
    m.setRowCount(0);

    boolean encontrado = false;

    for (Cliente c : cola.getCola()) {
        agregarFilaBusqueda(m, c);
        encontrado = true;
    }

    for (Cliente c : lista.getLista()) {
        agregarFilaBusqueda(m, c);
        encontrado = true;
    }

    if (!encontrado) {
        JOptionPane.showMessageDialog(vista,
                "No hay clientes registrados en este momento.",
                "Sin datos",
                JOptionPane.INFORMATION_MESSAGE);
    }
    }

    private void agregarFilaBusqueda(DefaultTableModel m, Cliente c) {
        m.addRow(new Object[]{
            c.getId(),
            c.getNombre(),
            c.getTipoSolicitud(),
            c.getPrioridad(),
            c.formato(c.getHoraLlegada()),
            c.getHoraSalida() == null ? "-" : c.formato(c.getHoraSalida()),
            c.getTiempoAtencion()
        });
    }

    // ==========================================================
    // TABLAS
    // ==========================================================

    private void actualizarTablas() {

        DefaultTableModel mCola = vista.getModelCola();
        mCola.setRowCount(0);

        for (Cliente c : cola.getCola()) {
            mCola.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getTipoSolicitud(),
                c.getPrioridad(),
                c.formato(c.getHoraLlegada())
            });
        }

        DefaultTableModel mAt = vista.getModelAtendidos();
        mAt.setRowCount(0);

        for (Cliente c : lista.getLista()) {
            mAt.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getTipoSolicitud(),
                c.getPrioridad(),
                c.formato(c.getHoraLlegada()),
                c.formato(c.getHoraSalida()),
                c.getTiempoAtencion()
            });
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
