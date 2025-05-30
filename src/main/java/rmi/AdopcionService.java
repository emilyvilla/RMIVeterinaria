package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdopcionService extends Remote {
    String registrarAdopcion(String nombreMascota) throws RemoteException;
}
