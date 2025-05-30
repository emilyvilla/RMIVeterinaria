/**
 *
 * @Emily Villa
 */
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AdopcionService extends Remote {
    String registrarMascotaAdopcion(String id, String nombre, String estado) throws RemoteException;
    List<String> listarMascotasAdopcion() throws RemoteException;
    String cambiarEstadoMascota(String id, String nuevoEstado) throws RemoteException;
    List<String> historialAdopciones(String cedula) throws RemoteException;
}
