/**
 *
 * @Emily Villa
 */
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FinanzasService extends Remote {
    String registrarPago(String cedula, String concepto, double monto) throws RemoteException;
    String generarReporteDiario() throws RemoteException;
    String generarReporteMensual() throws RemoteException;
    List<String> pagosPorMascota(String nombreMascota) throws RemoteException;
    String balanceGeneral() throws RemoteException;
}

