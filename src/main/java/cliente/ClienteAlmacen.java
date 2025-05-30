package cliente;

import rmi.InventarioService;

import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * ClienteAlmacen
 * Cliente para el personal de farmacia/almacén para registrar entradas y salidas de insumos.
 */
public class ClienteAlmacen {

    private InventarioService inventarioService;

    public ClienteAlmacen() {
        try {
            // Conexión al servidor administrativo (puerto 1100)
            Registry registry = LocateRegistry.getRegistry("localhost", 1100);
            inventarioService = (InventarioService) registry.lookup("InventarioService");

            iniciarGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Interfaz gráfica
    private void iniciarGUI() {
        JFrame frame = new JFrame("Farmacia / Almacén - Inventario");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel lblProducto = new JLabel("Nombre del Producto:");
        lblProducto.setBounds(20, 20, 150, 25);
        frame.add(lblProducto);

        JTextField txtProducto = new JTextField();
        txtProducto.setBounds(170, 20, 200, 25);
        frame.add(txtProducto);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(20, 60, 150, 25);
        frame.add(lblCantidad);

        JTextField txtCantidad = new JTextField();
        txtCantidad.setBounds(170, 60, 200, 25);
        frame.add(txtCantidad);

        JButton btnEntrada = new JButton("Registrar Entrada");
        btnEntrada.setBounds(170, 100, 150, 25);
        frame.add(btnEntrada);

        JButton btnSalida = new JButton("Registrar Salida");
        btnSalida.setBounds(330, 100, 150, 25);
        frame.add(btnSalida);

        JTextArea txtLog = new JTextArea();
        txtLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBounds(20, 150, 540, 150);
        frame.add(scrollLog);

        // Panel de alerta de inventario bajo
        JLabel lblAlerta = new JLabel("⚠ Productos con bajo inventario:");
        lblAlerta.setBounds(20, 310, 300, 25);
        frame.add(lblAlerta);

        JTextArea txtAlertas = new JTextArea();
        txtAlertas.setEditable(false);
        JScrollPane scrollAlerta = new JScrollPane(txtAlertas);
        scrollAlerta.setBounds(20, 340, 540, 100);
        frame.add(scrollAlerta);

        // Acción: Registrar entrada
        btnEntrada.addActionListener(e -> {
            try {
                String nombre = txtProducto.getText();
                int cantidad = Integer.parseInt(txtCantidad.getText());
                String respuesta = inventarioService.registrarProducto(nombre, cantidad);
                txtLog.append("✔ " + respuesta + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                txtLog.append("✖ Error al registrar entrada\n");
            }
        });

        // Acción: Registrar salida
        btnSalida.addActionListener(e -> {
            try {
                String nombre = txtProducto.getText();
                int cantidad = Integer.parseInt(txtCantidad.getText());
                String respuesta = inventarioService.registrarProducto(nombre, -cantidad); // salida = negativo
                txtLog.append("✔ " + respuesta + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                txtLog.append("✖ Error al registrar salida\n");
            }
        });

        // Simular alerta por productos bajos (en proyecto real vendría del servicio)
        txtAlertas.setText("• Amoxicilina - 3 unidades\n• Jeringas - 1 unidad\n• Vacuna rabia - 2 unidades");

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ClienteAlmacen();
    }
}
