package cliente;

import rmi.HistorialService;
import rmi.InventarioService;
import rmi.MascotaService;
import clases.Mascota;

import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClienteVeterinario {

    private HistorialService historialService;
    private InventarioService inventarioService;
    private MascotaService mascotaService;

    public ClienteVeterinario() {
        try {
            Registry regClinico = LocateRegistry.getRegistry("localhost", 1099);
            historialService = (HistorialService) regClinico.lookup("HistorialService");
            mascotaService = (MascotaService) regClinico.lookup("MascotaService");

            Registry regAdmin = LocateRegistry.getRegistry("localhost", 1100);
            inventarioService = (InventarioService) regAdmin.lookup("InventarioService");

            iniciarGUI();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con los servicios RMI:\n" + e.getMessage());
        }
    }

    private void iniciarGUI() {
        JFrame frame = new JFrame("Veterinario - Gestión Clínica");
        frame.setSize(700, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel lblMascota = new JLabel("Nombre de Mascota:");
        lblMascota.setBounds(20, 20, 150, 25);
        frame.add(lblMascota);

        JTextField txtMascota = new JTextField();
        txtMascota.setBounds(170, 20, 200, 25);
        frame.add(txtMascota);

        JLabel lblDetalle = new JLabel("Detalle Clínico:");
        lblDetalle.setBounds(20, 60, 150, 25);
        frame.add(lblDetalle);

        JTextArea txtDetalle = new JTextArea();
        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        scrollDetalle.setBounds(170, 60, 400, 100);
        frame.add(scrollDetalle);

        JButton btnGuardarHistorial = new JButton("Registrar Historial");
        btnGuardarHistorial.setBounds(170, 170, 200, 25);
        frame.add(btnGuardarHistorial);

        JButton btnVerHistorial = new JButton("Ver Historial");
        btnVerHistorial.setBounds(380, 170, 150, 25);
        frame.add(btnVerHistorial);

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

        JLabel lblVacuna = new JLabel("Nombre de Vacuna:");
        lblVacuna.setBounds(20, 300, 150, 25);
        frame.add(lblVacuna);

        JTextField txtVacuna = new JTextField();
        txtVacuna.setBounds(170, 300, 200, 25);
        frame.add(txtVacuna);

        JButton btnVacuna = new JButton("Registrar Vacuna Aplicada");
        btnVacuna.setBounds(380, 300, 200, 25);
        frame.add(btnVacuna);

        JTextArea txtLog = new JTextArea();
        txtLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBounds(20, 350, 640, 220);
        frame.add(scrollLog);

        // Acciones

        btnGuardarHistorial.addActionListener(e -> {
            try {
                String nombre = txtMascota.getText().trim();
                String detalle = txtDetalle.getText().trim();

                if (!nombre.isEmpty() && !detalle.isEmpty()) {
                    Mascota mascota = mascotaService.consultarMascotaPorNombre(nombre);
                    if (mascota == null) {
                        txtLog.append("✖ Mascota no encontrada: " + nombre + "\n");
                        return;
                    }

                    String idMascota = mascota.getId();
                    String respuesta = historialService.agregarEntradaMedica(idMascota, detalle);
                    txtLog.append("✔ " + respuesta + "\n");
                } else {
                    txtLog.append("✖ Debes ingresar nombre de mascota y detalle clínico\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                txtLog.append("✖ Error al registrar historial\n");
            }
        });

        btnVerHistorial.addActionListener(e -> {
            try {
                String nombre = txtMascota.getText().trim();
                if (nombre.isEmpty()) {
                    txtLog.append("✖ Ingresa el nombre de la mascota para ver el historial\n");
                    return;
                }

                Mascota mascota = mascotaService.consultarMascotaPorNombre(nombre);
                if (mascota == null) {
                    txtLog.append("✖ Mascota no encontrada: " + nombre + "\n");
                    return;
                }

                String idMascota = mascota.getId();
                List<String> historial = historialService.consultarHistorial(idMascota);

                txtLog.append("📘 Historial médico de " + nombre + ":\n");
                if (historial.isEmpty()) {
                    txtLog.append("   (No hay entradas registradas)\n");
                } else {
                    for (int i = 0; i < historial.size(); i++) {
                        txtLog.append("  " + (i + 1) + ". " + historial.get(i) + "\n");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                txtLog.append("✖ Error al consultar historial\n");
            }
        });

        btnSolicitar.addActionListener(e -> {
            try {
                String nombre = txtInsumo.getText().trim();
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                if (!nombre.isEmpty() && cantidad > 0) {
                    String respuesta = inventarioService.registrarProducto(nombre, -cantidad);
                    txtLog.append("✔ " + respuesta + "\n");
                } else {
                    txtLog.append("✖ Ingresa nombre de insumo y cantidad válida\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                txtLog.append("✖ Error al solicitar insumo\n");
            }
        });

        btnVacuna.addActionListener(e -> {
            try {
                String nombre = txtMascota.getText().trim();
                String vacuna = txtVacuna.getText().trim();

                if (!nombre.isEmpty() && !vacuna.isEmpty()) {
                    Mascota mascota = mascotaService.consultarMascotaPorNombre(nombre);
                    if (mascota == null) {
                        txtLog.append("✖ Mascota no encontrada: " + nombre + "\n");
                        return;
                    }

                    String idMascota = mascota.getId();
                    String entrada = "Vacuna aplicada: " + vacuna + " - " + java.time.LocalDate.now();
                    String respuesta = historialService.agregarEntradaMedica(idMascota, entrada);
                    txtLog.append("💉 " + respuesta + "\n");
                } else {
                    txtLog.append("✖ Ingresa nombre de mascota y vacuna\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                txtLog.append("✖ Error al registrar vacuna\n");
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteVeterinario::new);
    }
}
