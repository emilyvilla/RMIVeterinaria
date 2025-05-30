package cliente;

import rmi.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

/**
 * @Dayro
 * Cliente de Administración. Gestiona adopciones, finanzas y visualización
 * cruzada de servicios.
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
        // --- Panel de Adopciones ---
        JLabel lblID = new JLabel("ID Mascota:");
        lblID.setBounds(20, 20, 100, 25);
        frame.add(lblID);

        JTextField txtID = new JTextField();
        txtID.setBounds(110, 20, 80, 25);
        frame.add(txtID);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(200, 20, 60, 25);
        frame.add(lblNombre);

        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(260, 20, 100, 25);
        frame.add(txtNombre);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(370, 20, 60, 25);
        frame.add(lblEstado);

        JTextField txtEstado = new JTextField("disponible");
        txtEstado.setBounds(430, 20, 100, 25);
        frame.add(txtEstado);

        JButton btnAdopcion = new JButton("Registrar Adopción");
        btnAdopcion.setBounds(540, 20, 160, 25);
        frame.add(btnAdopcion);

        // --- Panel Financiero ---
        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(20, 60, 60, 25);
        frame.add(lblCedula);

        JTextField txtCedula = new JTextField();
        txtCedula.setBounds(80, 60, 100, 25);
        frame.add(txtCedula);

        JLabel lblConcepto = new JLabel("Concepto:");
        lblConcepto.setBounds(190, 60, 70, 25);
        frame.add(lblConcepto);

        JTextField txtConcepto = new JTextField();
        txtConcepto.setBounds(260, 60, 120, 25);
        frame.add(txtConcepto);

        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setBounds(390, 60, 50, 25);
        frame.add(lblMonto);

        JTextField txtMonto = new JTextField();
        txtMonto.setBounds(440, 60, 80, 25);
        frame.add(txtMonto);

        JButton btnPago = new JButton("Registrar Pago");
        btnPago.setBounds(530, 60, 150, 25);
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
        // --- Acción del botón para registrar adopción ---
        btnAdopcion.addActionListener(e -> {
            try {
                String id = txtID.getText().trim();
                String nombre = txtNombre.getText().trim();
                String estado = txtEstado.getText().trim();

                if (id.isEmpty() || nombre.isEmpty() || estado.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Por favor, completa todos los campos.");
                    return;
                }

                String resultado = adopcionService.registrarMascotaAdopcion(id, nombre, estado);
                JOptionPane.showMessageDialog(frame, resultado);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error al registrar mascota en adopción.");
            }
        });

        btnPago.addActionListener(e -> {
            try {
                String cedula = txtCedula.getText().trim();
                String concepto = txtConcepto.getText().trim();
                String montoStr = txtMonto.getText().trim();

                if (cedula.isEmpty() || concepto.isEmpty() || montoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Por favor, completa todos los campos.");
                    return;
                }

                double monto = Double.parseDouble(montoStr);
                String resultado = finanzasService.registrarPago(cedula, concepto, monto);
                JOptionPane.showMessageDialog(frame, resultado);

                txtReportes.append("Pago registrado: " + concepto + " - $" + monto + " (Cédula: " + cedula + ")\n");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame, "El monto debe ser un número válido.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error al registrar el pago.");
            }
        });

        btnVistaGeneral.addActionListener(e -> {
            try {
                txtVista.setText("");

                // Mostrar todas las citas
                String citasTexto = citaService.verCitas();
                txtVista.append("Citas:\n" + citasTexto + "\n");

                // Mostrar historial médico de ejemplo (mascota con ID "1")
                List<String> historial = historialService.consultarHistorial("1");
                txtVista.append("\nHistorial Médico (Mascota ID 1):\n");
                for (String entrada : historial) {
                    txtVista.append("- " + entrada + "\n");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al consultar vista general.");
            }
        });

        actualizarInventario(); // Cargar inventario al iniciar
        frame.setVisible(true);
    }

    // Método para mostrar el inventario actual
    private void actualizarInventario() {
        try {
            List<String> productos = inventarioService.listarProductos();
            inventarioModel.setRowCount(0); // Limpiar tabla

            for (String prod : productos) {
                // Suponiendo formato: "Producto - Cantidad"
                String[] partes = prod.split(" - ");
                if (partes.length == 2) {
                    String nombre = partes[0].trim();
                    int cantidad = Integer.parseInt(partes[1].trim());
                    inventarioModel.addRow(new Object[]{nombre, cantidad});
                }
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
