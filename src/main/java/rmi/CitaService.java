/**
 *
 * @author Emily Villa
 */
package rmi;

import clases.Cita;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CitaService extends Remote {
    String agendarCita(String cedula, String mascota, String fecha) throws RemoteException;
    String modificarCita(String cedula, String nuevaFecha) throws RemoteException;
    String cancelarCita(String cedula) throws RemoteException;
    List<Cita> obtenerCitasPorMascota(String idMascota) throws RemoteException;
    List<Cita> obtenerCitasDelDia(String fecha) throws RemoteException;
    String verCitas() throws RemoteException;
}
