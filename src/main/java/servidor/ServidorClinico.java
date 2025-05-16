/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import rmi.*;

/**
 *
 * @author 1
 */
public class ServidorClinico extends UnicastRemoteObject implements MascotaService, CitaService, HistorialService {

    public ServidorClinico() throws Exception { super(); }

    // Métodos de MascotaService
    @Override
    public String registrarMascota(String nombre, String especie) {
        return "Mascota registrada: " + nombre + " (" + especie + ")";
    }

    @Override
    public String actualizarMascota(String id, String datos) {
        return "Datos actualizados para mascota ID: " + id;
    }

    // Métodos de CitaService
    public String agendarCita(String mascota, String fecha) {
        return "Cita agendada para " + mascota + " el " + fecha;
    }

    // Métodos de HistorialService
    public String agregarHistorial(String mascota, String detalle) {
        return "Historial actualizado: " + detalle;
    }

    public static void main(String[] args) {
        try {
            ServidorClinico servidor = new ServidorClinico();
            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("MascotaService", servidor);
            registry.rebind("CitaService", servidor);
            registry.rebind("HistorialService", servidor);

            System.out.println("Servidor Clínico iniciado en puerto 1099.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String modificarCita(String citaId, String nuevaFecha) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String cancelarCita(String citaId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
