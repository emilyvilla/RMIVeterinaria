/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cliente;
import rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 *
 * @author 1
 */
public class ClienteRecepcion { 
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            MascotaService mascotaService = (MascotaService) registry.lookup("MascotaService");
            CitaService citaService = (CitaService) registry.lookup("CitaService");

            System.out.println(mascotaService.registrarMascota("Firulais", "Perro"));
            System.out.println(citaService.agendarCita("Firulais", "2025-05-16"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}