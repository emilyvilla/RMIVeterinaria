/**
 *
 * @author Emily Villa
 */
package cliente;

import rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;

public class ClienteRecepcion {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            MascotaService mascotaService = (MascotaService) registry.lookup("MascotaService");
            CitaService citaService = (CitaService) registry.lookup("CitaService");

            String[] opciones = {
                "Registrar mascota y agendar cita", 
                "Modificar cita", 
                "Cancelar cita", 
                "Ver citas", 
                "Salir"
            };

            while (true) {
                int seleccion = JOptionPane.showOptionDialog(null, "Seleccione una opción:",
                        "Menú", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, opciones, opciones[0]);

                switch (seleccion) {
                    case 0 -> {
                        String cedula = JOptionPane.showInputDialog(null, "Ingrese la cédula del dueño:");
                        String nombreMascota = JOptionPane.showInputDialog(null, "Ingrese el nombre de la mascota:");
                        String tipoMascota = JOptionPane.showInputDialog(null, "Ingrese el tipo de mascota (ej. Perro, Gato):");
                        String fechaCita = JOptionPane.showInputDialog(null, "Ingrese la fecha de la cita (YYYY-MM-DD):");

                        String resultadoMascota = mascotaService.registrarMascota(nombreMascota, tipoMascota);
                        String resultadoCita = citaService.agendarCita(cedula, nombreMascota, fechaCita);

                        JOptionPane.showMessageDialog(null, resultadoMascota + "\n" + resultadoCita);
                    }
                    case 1 -> {
                        String cedula = JOptionPane.showInputDialog(null, "Ingrese la cédula del dueño de la cita a modificar:");
                        String nuevaFecha = JOptionPane.showInputDialog(null, "Ingrese la nueva fecha (YYYY-MM-DD):");

                        String resultado = citaService.modificarCita(cedula, nuevaFecha);
                        JOptionPane.showMessageDialog(null, resultado);
                    }
                    case 2 -> {
                        String cedula = JOptionPane.showInputDialog(null, "Ingrese la cédula del dueño de la cita a cancelar:");
                        String resultado = citaService.cancelarCita(cedula);
                        JOptionPane.showMessageDialog(null, resultado);
                    }
                    case 3 -> {
                        String citas = citaService.verCitas();
                        JOptionPane.showMessageDialog(null, citas.isEmpty() ? "No hay citas registradas." : citas);
                    }
                    case 4, JOptionPane.CLOSED_OPTION -> {
                        JOptionPane.showMessageDialog(null, "Saliendo del sistema.");
                        return; // Termina el programa
                    }
                    default -> JOptionPane.showMessageDialog(null, "Opción no válida.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
