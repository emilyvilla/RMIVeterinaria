
/**
 *
 * @Emily Villa
 */

package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InventarioService extends Remote {
    String registrarProducto(String nombre, int cantidad) throws RemoteException;
    String registrarEntrada(String nombre, int cantidad) throws RemoteException;
    String registrarSalida(String nombre, int cantidad) throws RemoteException;
    int consultarStock(String nombre) throws RemoteException;
    List<String> listarProductos() throws RemoteException;
}
