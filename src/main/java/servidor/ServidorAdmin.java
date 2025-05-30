/**
 *
 * @author Emily Villa
 */
package servidor;


import clases.MascotaAdopcion;
import clases.Pago;
import clases.Producto;
import rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.*;

public class ServidorAdmin extends UnicastRemoteObject implements InventarioService, AdopcionService, FinanzasService {

    private final Map<String, Producto> inventario = new HashMap<>();
    private final Map<String, MascotaAdopcion> mascotasAdopcion = new HashMap<>();
    private final Map<String, List<String>> adopcionesPorUsuario = new HashMap<>();
    private final List<Pago> pagos = new ArrayList<>();

    public ServidorAdmin() throws Exception {}

    // ------ InventarioService ------
    public String registrarProducto(String nombre, int cantidad) {
        inventario.put(nombre, new Producto(nombre, cantidad));
        return "Producto registrado: " + nombre + " con cantidad " + cantidad;
    }

    public String registrarEntrada(String nombre, int cantidad) {
        Producto p = inventario.get(nombre);
        if (p == null) return "Producto no encontrado.";
        p.stock += cantidad;
        return "Entrada registrada. Nuevo stock: " + p.stock;
    }

    public String registrarSalida(String nombre, int cantidad) {
        Producto p = inventario.get(nombre);
        if (p == null) return "Producto no encontrado.";
        if (p.stock < cantidad) return "Stock insuficiente.";
        p.stock -= cantidad;
        return "Salida registrada. Stock restante: " + p.stock;
    }

    public int consultarStock(String nombre) {
        Producto p = inventario.get(nombre);
        return (p != null) ? p.stock : 0;
    }

    public List<String> listarProductos() {
        List<String> lista = new ArrayList<>();
        for (Producto p : inventario.values()) {
            lista.add(p.nombre + " - Stock: " + p.stock);
        }
        return lista;
    }

    // ------- AdopcionService -----
    public String registrarMascotaAdopcion(String id, String nombre, String estado) {
        mascotasAdopcion.put(id, new MascotaAdopcion(id, nombre, estado));
        return "Mascota registrada para adopción: " + nombre + " (" + estado + ")";
    }

    public List<String> listarMascotasAdopcion() {
        List<String> lista = new ArrayList<>();
        for (MascotaAdopcion m : mascotasAdopcion.values()) {
            if (!m.estado.equalsIgnoreCase("Adoptada")) {
                lista.add(m.id + ": " + m.nombre + " - Estado: " + m.estado);
            }
        }
        return lista;
    }

    public String cambiarEstadoMascota(String id, String nuevoEstado) {
        MascotaAdopcion m = mascotasAdopcion.get(id);
        if (m == null) return "Mascota no encontrada.";
        m.estado = nuevoEstado;
        return "Estado actualizado para " + m.nombre + " a " + nuevoEstado;
    }

    public List<String> historialAdopciones(String cedula) {
        return adopcionesPorUsuario.getOrDefault(cedula, new ArrayList<>());
    }

    // -------- FinanzasService ---
    public String registrarPago(String cedula, String concepto, double monto) {
        String fecha = LocalDate.now().toString();
        pagos.add(new Pago(cedula, concepto, monto, fecha));
        return "Pago registrado: " + concepto + " - $" + monto;
    }

    public String generarReporteDiario() {
        String hoy = LocalDate.now().toString();
        double total = pagos.stream().filter(p -> p.fecha.equals(hoy)).mapToDouble(p -> p.monto).sum();
        return "Reporte diario (" + hoy + "): $" + total;
    }

    public String generarReporteMensual() {
        String mes = LocalDate.now().toString().substring(0, 7);
        double total = pagos.stream().filter(p -> p.fecha.startsWith(mes)).mapToDouble(p -> p.monto).sum();
        return "Reporte mensual (" + mes + "): $" + total;
    }

    public List<String> pagosPorMascota(String nombreMascota) {
        List<String> resultado = new ArrayList<>();
        for (Pago p : pagos) {
            if (p.concepto.contains(nombreMascota)) {
                resultado.add(p.fecha + ": $" + p.monto + " - " + p.concepto);
            }
        }
        return resultado;
    }

    public String balanceGeneral() {
        double total = pagos.stream().mapToDouble(p -> p.monto).sum();
        return "Balance general: $" + total;
    }

    public static void main(String[] args) {
        try {
            ServidorAdmin servidor = new ServidorAdmin();
            Registry registry = LocateRegistry.createRegistry(1100);
            registry.rebind("InventarioService", servidor);
            registry.rebind("AdopcionService", servidor);
            registry.rebind("FinanzasService", servidor);
            System.out.println("Servidor Administrativo iniciado en puerto 1100.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
