/**
 *
 * @author Emily Villa
 */
package servidor;

import clases.Cita;
import clases.Mascota;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import rmi.*;

import java.util.*;

public class ServidorClinico extends UnicastRemoteObject implements MascotaService, CitaService, HistorialService {

    private final Map<String, Mascota> mascotas;
    private final Map<String, Cita> citas;
    private final Map<String, List<String>> historiales = new HashMap<>();

    private int mascotaCounter = 1;

    public ServidorClinico() throws RemoteException {
        super();
        mascotas = new HashMap<>();
        citas = new HashMap<>();
    }

    // ----- MascotaService -----
    @Override
    public String registrarMascota(String nombre, String especie) {
        String id = "M" + (mascotaCounter++);
        Mascota mascota = new Mascota(id, nombre, especie, "sin_cedula");
        mascotas.put(id, mascota);
        return "Mascota registrada con ID: " + id;
    }

    @Override
    public String actualizarMascota(String id, String datos) {
        return "Actualización no implementada.";
    }

    @Override
    public String eliminarMascota(String id) {
        mascotas.remove(id);
        return "Mascota eliminada.";
    }

    @Override
    public Mascota consultarMascota(String id) {
        return mascotas.get(id);
    }
    
    @Override
public Mascota consultarMascotaPorNombre(String nombre) throws RemoteException {
    for (Mascota m : mascotas.values()) {
        if (m.getNombre().equalsIgnoreCase(nombre)) {
            return m;
        }
    }
    return null;
}


    @Override
    public List<Mascota> listarMascotasPorDueno(String cedula) {
        List<Mascota> resultado = new ArrayList<>();
        for (Mascota m : mascotas.values()) {
            if (m.getCedulaDueno().equals(cedula)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    // --- Cita Service -----
    @Override
    public String agendarCita(String cedula, String nombreMascota, String fecha) {
        String idMascota = null;
        for (Mascota m : mascotas.values()) {
            if (m.getNombre().equals(nombreMascota)) {
                idMascota = m.getId();
                break;
            }
        }
        if (idMascota == null) {
            return "Mascota no registrada.";
        }

        if (citas.containsKey(cedula)) {
            return "Ya existe una cita registrada con esa cédula.";
        }

        Cita cita = new Cita(cedula, idMascota, fecha);
        citas.put(cedula, cita);
        return "Cita registrada: " + cita;
    }

    @Override
    public String modificarCita(String cedula, String nuevaFecha) {
        Cita cita = citas.get(cedula);
        if (cita == null) {
            return "No se encontró la cita.";
        }
        cita = new Cita(cedula, cita.getIdMascota(), nuevaFecha);
        citas.put(cedula, cita);
        return "Cita modificada.";
    }

    @Override
    public String cancelarCita(String cedula) {
        if (citas.remove(cedula) != null) {
            return "Cita cancelada.";
        } else {
            return "No se encontró la cita.";
        }
    }

    @Override
    public List<Cita> obtenerCitasPorMascota(String idMascota) {
        List<Cita> resultado = new ArrayList<>();
        for (Cita c : citas.values()) {
            if (c.getIdMascota().equals(idMascota)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public List<Cita> obtenerCitasDelDia(String fecha) {
        List<Cita> resultado = new ArrayList<>();
        for (Cita c : citas.values()) {
            if (c.getFecha().equals(fecha)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public String verCitas() {
        if (citas.isEmpty()) {
            return "No hay citas registradas.";
        }
        StringBuilder sb = new StringBuilder();
        for (Cita c : citas.values()) {
            sb.append(c.toString()).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            ServidorClinico servidor = new ServidorClinico();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MascotaService", servidor);
            registry.rebind("CitaService", servidor);
            registry.rebind("HistorialService", servidor);
            System.out.println("Servidor iniciado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------Historial Service---------
    @Override
    public String agregarEntradaMedica(String idMascota, String entrada) throws RemoteException {
        historiales.putIfAbsent(idMascota, new ArrayList<>());
        historiales.get(idMascota).add(entrada);
        return "Entrada médica agregada para la mascota " + idMascota;
    }

    @Override
    public String actualizarEntradaMedica(String idMascota, int indice, String nuevaEntrada) throws RemoteException {
        List<String> historial = historiales.get(idMascota);
        if (historial == null) {
            return "No existe historial para esta mascota.";
        }
        if (indice < 0 || indice >= historial.size()) {
            return "Índice fuera de rango.";
        }
        historial.set(indice, nuevaEntrada);
        return "Entrada médica actualizada para la mascota " + idMascota;
    }

    @Override
    public List<String> consultarHistorial(String idMascota) throws RemoteException {
        return historiales.getOrDefault(idMascota, new ArrayList<>());
    }

    @Override
    public String agregarHistorial(String mascota, String detalle) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
public List<Cita> obtenerTodasLasCitas() throws RemoteException {
    return new ArrayList<>(citas.values());
}

}
