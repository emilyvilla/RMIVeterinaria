/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import rmi.*;
/**
 *
 * @author 1
 */
public class ServidorAdmin extends UnicastRemoteObject implements InventarioService, AdopcionService, FinanzasService {

    public ServidorAdmin() throws Exception { super(); }

    // InventarioService
    public String registrarProducto(String nombre, int cantidad) {
        return "Producto registrado: " + nombre + ", cantidad: " + cantidad;
    }

    // AdopcionService
    public String registrarAdopcion(String mascota) {
        return "Mascota registrada para adopción: " + mascota;
    }

    // FinanzasService
    public String registrarPago(String concepto, double monto) {
        return "Pago registrado por " + concepto + ": $" + monto;
    }

    public static void main(String[] args) {
        try {
            ServidorAdmin servidor = new ServidorAdmin();
            Registry registry = LocateRegistry.createRegistry(1100);

            registry.rebind("InventarioService", servidor);
            registry.rebind("AdopcionService", servidor);
            registry.rebind("FinanzasService", servidor);

            System.out.println("Servidor Administrativo iniciado en puerto 1100.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}