package cliente;

import rmi.HistorialService;
import rmi.InventarioService;

import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * ClienteVeterinario Cliente para que los veterinarios registren diagnósticos,
 * tratamientos y solicitudes de insumos.
 */
public class ClienteVeterinario {

    private HistorialService historialService;
    private InventarioService inventarioService;

    public ClienteVeterinario() {
        try {
            // Conexión al servidor clínico (puerto 1099) para historial
            Registry regClinico = LocateRegistry.getRegistry("localhost", 1099);
            historialService = (HistorialService) regClinico.lookup("HistorialService");

            // Conexión al servidor administrativo (puerto 1100) para inventario
            Registry regAdmin = LocateRegistry.getRegistry("localhost", 1100);
            inventarioService = (InventarioService) regAdmin.lookup("InventarioService");

            iniciarGUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Interfaz gráfica
    private void iniciarGUI() {
        JFrame frame = new JFrame("Veterinario - Gestión Clínica");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Campo para ingresar nombre de la mascota
        JLabel lblMascota = new JLabel("Nombre de Mascota:");
        lblMascota.setBounds(20, 20, 150, 25);
        frame.add(lblMascota);

        JTextField txtMascota = new JTextField();
        txtMascota.setBounds(170, 20, 200, 25);
        frame.add(txtMascota);

        // Campo para ingresar diagnóstico / tratamiento
        JLabel lblDetalle = new JLabel("Detalle Clínico:");
        lblDetalle.setBounds(20, 60, 150, 25);
        frame.add(lblDetalle);

        JTextArea txtDetalle = new JTextArea();
        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        scrollDetalle.setBounds(170, 60, 300, 100);
        frame.add(scrollDetalle);

        JButton btnGuardarHistorial = new JButton("Registrar Historial");
        btnGuardarHistorial.setBounds(170, 170, 200, 25);
        frame.add(btnGuardarHistorial);

        // Campo para solicitar insumos
        JLabel lblInsumo = new JLabel("Solicitar Insumo:");
        lblInsumo.setBounds(20, 220, 150, 25);
        frame.add(lblInsumo);

        JTextField txtInsumo = new JTextField();
        txtInsumo.setBounds(170, 220, 200, 25);
        frame.add(txtInsumo);

        JTextField txtCantidad = new JTextField("1");
        txtCantidad.setBounds(380, 220, 50, 25);
        frame.add(txtCantidad);

        JButton btnSolicitar = new JButton("Solicitar al Almacén");
        btnSolicitar.setBounds(170, 260, 200, 25);
        frame.add(btnSolicitar);

        JTextArea txtLog = new JTextArea();
        txtLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBounds(20, 300, 540, 140);
        frame.add(scrollLog);

        // Acción: Registrar historial
        // Acción: Registrar historial
        btnGuardarHistorial.addActionListener(e -> {
            try {
                String mascota = txtMascota.getText();
                String detalle = txtDetalle.getText();
                String respuesta = historialService.agregarEntradaMedica(mascota, detalle);
                txtLog.append("✔ " + respuesta + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                txtLog.append("✖ Error al registrar historial\n");
            }
        });

        // Acción: Solicitar insumo
        btnSolicitar.addActionListener(e -> {
            try {
                String nombre = txtInsumo.getText();
                int cantidad = Integer.parseInt(txtCantidad.getText());
                String respuesta = inventarioService.registrarProducto(nombre, -cantidad); // salida = negativo
                txtLog.append("✔ " + respuesta + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                txtLog.append("✖ Error al solicitar insumo\n");
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ClienteVeterinario();
    }
}
