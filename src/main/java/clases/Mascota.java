package clases;

import java.io.Serializable;

public class Mascota implements Serializable {
    private String id;
    private String nombre;
    private String especie;
    private String cedulaDueno;

    public Mascota(String id, String nombre, String especie, String cedulaDueno) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.cedulaDueno = cedulaDueno;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getCedulaDueno() { return cedulaDueno; }

    @Override
    public String toString() {
        return nombre + " (" + especie + ") - ID: " + id + ", Dueño: " + cedulaDueno;
    }
}
