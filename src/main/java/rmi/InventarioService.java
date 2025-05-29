package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface InventarioService extends Remote {
    Map<String, Integer> obtenerInventario() throws RemoteException;
}
