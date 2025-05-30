package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface InventarioService extends Remote {
    Map<String, Integer> obtenerInventario() throws RemoteException;

    String registrarProducto(String nombre, int cantidad) throws RemoteException;

    int consultarStock(String nombre) throws RemoteException;
}
