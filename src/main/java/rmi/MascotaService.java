package rmi;

import clases.Mascota;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MascotaService extends Remote {
    String registrarMascota(String nombre, String especie) throws RemoteException;
    String actualizarMascota(String id, String datos) throws RemoteException;
    String eliminarMascota(String id) throws RemoteException;
    Mascota consultarMascota(String id) throws RemoteException;
    List<Mascota> listarMascotasPorDueno(String cedula) throws RemoteException;

    Mascota consultarMascotaPorNombre(String nombre) throws RemoteException;
}
