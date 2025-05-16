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

public interface MascotaService extends Remote {
    String registrarMascota(String nombre, String especie) throws RemoteException;
    String actualizarMascota(String id, String datos) throws RemoteException;
}
