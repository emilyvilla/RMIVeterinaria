/**
 *
 * @Emily Villa
 */
package clases;


public class MascotaAdopcion {
    public String id;
    public String nombre;
    public String estado; // Disponible, En proceso, Adoptada

    public MascotaAdopcion(String id, String nombre, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }
}
