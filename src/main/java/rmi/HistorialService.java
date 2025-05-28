package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HistorialService extends Remote {
    String agregarEntradaMedica(String idMascota, String entrada) throws RemoteException;
    String actualizarEntradaMedica(String idMascota, int indice, String nuevaEntrada) throws RemoteException;
    List<String> consultarHistorial(String idMascota) throws RemoteException;
}
