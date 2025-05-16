/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author 1
 */

public interface CitaService extends Remote {
    String agendarCita(String mascotaId, String fecha) throws RemoteException;
    String modificarCita(String citaId, String nuevaFecha) throws RemoteException;
    String cancelarCita(String citaId) throws RemoteException;
}
