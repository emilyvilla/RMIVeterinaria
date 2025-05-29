package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface FinanzasService extends Remote {
    String registrarPago(String concepto, double monto) throws RemoteException;
    Map<String, Double> obtenerPagos() throws RemoteException;
    double obtenerBalance() throws RemoteException;
}
