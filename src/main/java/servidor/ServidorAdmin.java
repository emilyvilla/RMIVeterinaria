package servidor;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import rmi.*;

public class ServidorAdmin extends UnicastRemoteObject implements InventarioService, AdopcionService, FinanzasService {

    public ServidorAdmin() throws Exception { super(); }
    private final Map<String, Integer> inventario = new java.util.HashMap<>();


    // InventarioService
   @Override
public synchronized String registrarProducto(String nombre, int cantidad) {
    inventario.put(nombre, inventario.getOrDefault(nombre, 0) + cantidad);
    return "Producto actualizado: " + nombre + ", cantidad actual: " + inventario.get(nombre);
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

    @Override
public synchronized Map<String, Integer> obtenerInventario() throws RemoteException {
    return new java.util.HashMap<>(inventario); // devolver una copia para evitar que se modifique remotamente
}


    @Override
    public Map<String, Double> obtenerPagos() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double obtenerBalance() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
public synchronized int consultarStock(String nombre) throws RemoteException {
    return inventario.getOrDefault(nombre, 0);
}
}
