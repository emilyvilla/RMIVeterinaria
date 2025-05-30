package cliente;

import clases.Cita;
import rmi.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
/**
 * Cliente de Administración.
 * Gestiona adopciones, finanzas y visualización cruzada de servicios.
 */
public class ClienteAdministracion {

    private AdopcionService adopcionService;
    private FinanzasService finanzasService;
    private InventarioService inventarioService;
    private CitaService citaService;
    private HistorialService historialService;

    private DefaultTableModel inventarioModel;
    private JTextArea txtReportes;

    public ClienteAdministracion() {
        try {
            // Conexión al Servidor Administrativo (puerto 1100)
            Registry regAdmin = LocateRegistry.getRegistry("localhost", 1100);
            adopcionService = (AdopcionService) regAdmin.lookup("AdopcionService");
            finanzasService = (FinanzasService) regAdmin.lookup("FinanzasService");
            inventarioService = (InventarioService) regAdmin.lookup("InventarioService");

            // Conexión al Servidor Clínico (puerto 1099)
            Registry regClinico = LocateRegistry.getRegistry("localhost", 1099);
            citaService = (CitaService) regClinico.lookup("CitaService");
            historialService = (HistorialService) regClinico.lookup("HistorialService");

            iniciarGUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Interfaz gráfica principal
    private void iniciarGUI() {
        JFrame frame = new JFrame("Cliente de Administración");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // --- Panel de Adopciones ---
        JLabel lblAdopcion = new JLabel("Mascota en Adopción:");
        lblAdopcion.setBounds(20, 20, 150, 25);
        frame.add(lblAdopcion);

        JTextField txtMascotaAdop = new JTextField();
        txtMascotaAdop.setBounds(170, 20, 200, 25);
        frame.add(txtMascotaAdop);

        JButton btnAdopcion = new JButton("Registrar Adopción");
        btnAdopcion.setBounds(390, 20, 180, 25);
        frame.add(btnAdopcion);

        // --- Panel Financiero ---
        JLabel lblPago = new JLabel("Concepto:");
        lblPago.setBounds(20, 60, 100, 25);
        frame.add(lblPago);

        JTextField txtConcepto = new JTextField();
        txtConcepto.setBounds(100, 60, 150, 25);
        frame.add(txtConcepto);

        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setBounds(260, 60, 50, 25);
        frame.add(lblMonto);

        JTextField txtMonto = new JTextField();
        txtMonto.setBounds(310, 60, 100, 25);
        frame.add(txtMonto);

        JButton btnPago = new JButton("Registrar Pago");
        btnPago.setBounds(420, 60, 150, 25);
        frame.add(btnPago);

        // --- Reportes ---
        txtReportes = new JTextArea();
        txtReportes.setEditable(false);
        JScrollPane scrollReportes = new JScrollPane(txtReportes);
        scrollReportes.setBounds(20, 100, 650, 120);
        frame.add(scrollReportes);

        // --- Vista de Inventario ---
        JLabel lblInventario = new JLabel("Inventario:");
        lblInventario.setBounds(20, 230, 100, 25);
        frame.add(lblInventario);

        inventarioModel = new DefaultTableModel(new Object[]{"Producto", "Cantidad"}, 0);
        JTable table = new JTable(inventarioModel);
        JScrollPane scrollInv = new JScrollPane(table);
        scrollInv.setBounds(20, 260, 300, 200);
        frame.add(scrollInv);

        // --- Botón para vista general ---
        JButton btnVistaGeneral = new JButton("Vista General (Citas + Historial)");
        btnVistaGeneral.setBounds(350, 260, 250, 25);
        frame.add(btnVistaGeneral);

        JTextArea txtVista = new JTextArea();
        txtVista.setEditable(false);
        JScrollPane scrollVista = new JScrollPane(txtVista);
        scrollVista.setBounds(350, 300, 320, 160);
        frame.add(scrollVista);

        // --- Acciones de botones ---
        btnAdopcion.addActionListener(e -> {
            try {
                String mascota = txtMascotaAdop.getText();
                String resultado = adopcionService.registrarAdopcion(mascota);
                JOptionPane.showMessageDialog(frame, resultado);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnPago.addActionListener(e -> {
            try {
                String concepto = txtConcepto.getText();
                double monto = Double.parseDouble(txtMonto.getText());
                String resultado = finanzasService.registrarPago(concepto, monto);
                JOptionPane.showMessageDialog(frame, resultado);
                txtReportes.append("Pago registrado: " + concepto + " - $" + monto + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

btnVistaGeneral.addActionListener(e -> {
    try {
        txtVista.setText("");

        txtVista.append("=== Citas y Historial Médico ===\n\n");

        // Obtener todas las citas desde el servidor
        for (Cita cita : citaService.obtenerTodasLasCitas()) {
            String cedula = cita.getCedula();
            String idMascota = cita.getIdMascota();
            String fecha = cita.getFecha();

            txtVista.append("Cita:\n");
            txtVista.append("  • Cédula: " + cedula + "\n");
            txtVista.append("  • ID Mascota: " + idMascota + "\n");
            txtVista.append("  • Fecha: " + fecha + "\n");

            // Consultar historial de esa mascota
            java.util.List<String> historial = historialService.consultarHistorial(idMascota);

            if (historial.isEmpty()) {
                txtVista.append("  Historial: Sin registros.\n");
            } else {
                txtVista.append("  Historial:\n");
                for (String entrada : historial) {
                    txtVista.append("    - " + entrada + "\n");
                }
            }
            txtVista.append("\n");
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        txtVista.setText("Error al obtener las citas e historiales.");
    }
});



        actualizarInventario(); // Cargar inventario al iniciar
        frame.setVisible(true);
    }

    // Método para mostrar el inventario actual
    private void actualizarInventario() {
        try {
            Map<String, Integer> inv = inventarioService.obtenerInventario();
            inventarioModel.setRowCount(0);
            for (Map.Entry<String, Integer> entry : inv.entrySet()) {
                inventarioModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método principal
    public static void main(String[] args) {
        new ClienteAdministracion();
    }
}
