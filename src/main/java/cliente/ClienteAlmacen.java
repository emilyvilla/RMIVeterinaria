package cliente;

import rmi.InventarioService;

import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * ClienteAlmacen
 * Cliente para el personal de farmacia/almacén para registrar entradas, salidas y ver inventario.
 */
public class ClienteAlmacen {

    private InventarioService inventarioService;

    public ClienteAlmacen() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1100);
            inventarioService = (InventarioService) registry.lookup("InventarioService");

            iniciarGUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con InventarioService");
        }
    }

    private void iniciarGUI() {
        JFrame frame = new JFrame("Farmacia / Almacén - Gestión de Inventario");
        frame.setSize(650, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Nombre producto
        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setBounds(20, 20, 100, 25);
        frame.add(lblProducto);

        JTextField txtProducto = new JTextField();
        txtProducto.setBounds(120, 20, 200, 25);
        frame.add(txtProducto);

        // Cantidad
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(20, 60, 100, 25);
        frame.add(lblCantidad);

        JTextField txtCantidad = new JTextField();
        txtCantidad.setBounds(120, 60, 200, 25);
        frame.add(txtCantidad);

        // Botones
        JButton btnEntrada = new JButton("Registrar Entrada");
        btnEntrada.setBounds(340, 20, 170, 25);
        frame.add(btnEntrada);

        JButton btnSalida = new JButton("Registrar Salida");
        btnSalida.setBounds(340, 60, 170, 25);
        frame.add(btnSalida);

        JButton btnVerStock = new JButton("Consultar Stock");
        btnVerStock.setBounds(340, 100, 170, 25);
        frame.add(btnVerStock);

        // Log de operaciones
        JTextArea txtLog = new JTextArea();
        txtLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBounds(20, 140, 590, 200);
        frame.add(scrollLog);

        // Alerta de inventario bajo
        JLabel lblAlerta = new JLabel("⚠ Productos con inventario bajo:");
        lblAlerta.setBounds(20, 350, 250, 25);
        frame.add(lblAlerta);

        JTextArea txtAlertas = new JTextArea();
        txtAlertas.setEditable(false);
        JScrollPane scrollAlerta = new JScrollPane(txtAlertas);
        scrollAlerta.setBounds(20, 380, 590, 150);
        frame.add(scrollAlerta);

        // Acción: Registrar Entrada
        btnEntrada.addActionListener(e -> {
            try {
                String nombre = txtProducto.getText().trim();
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                if (!nombre.isEmpty() && cantidad > 0) {
                    String respuesta = inventarioService.registrarProducto(nombre, cantidad);
                    txtLog.append("✔ " + respuesta + "\n");
                } else {
                    txtLog.append("✖ Ingresa un producto válido y cantidad > 0\n");
                }
            } catch (Exception ex) {
                txtLog.append("✖ Error al registrar entrada\n");
                ex.printStackTrace();
            }
        });

        // Acción: Registrar Salida
        btnSalida.addActionListener(e -> {
            try {
                String nombre = txtProducto.getText().trim();
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                if (!nombre.isEmpty() && cantidad > 0) {
                    String respuesta = inventarioService.registrarProducto(nombre, -cantidad);
                    txtLog.append("✔ " + respuesta + "\n");
                } else {
                    txtLog.append("✖ Ingresa un producto válido y cantidad > 0\n");
                }
            } catch (Exception ex) {
                txtLog.append("✖ Error al registrar salida\n");
                ex.printStackTrace();
            }
        });

        // Acción: Consultar stock
        btnVerStock.addActionListener(e -> {
            try {
                String nombre = txtProducto.getText().trim();
                if (!nombre.isEmpty()) {
                    int stock = inventarioService.consultarStock(nombre);
                    txtLog.append("ℹ Stock actual de '" + nombre + "': " + stock + " unidades\n");
                } else {
                    txtLog.append("✖ Ingresa un nombre de producto para consultar\n");
                }
            } catch (Exception ex) {
                txtLog.append("✖ Error al consultar stock\n");
                ex.printStackTrace();
            }
        });

        // Alerta de productos con bajo inventario (simulado)
        txtAlertas.setText("""
            • Amoxicilina - 3 unidades
            • Jeringas - 1 unidad
            • Vacuna rabia - 2 unidades
        """);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteAlmacen::new);
    }
}
