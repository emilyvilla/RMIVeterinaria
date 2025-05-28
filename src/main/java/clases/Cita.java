package clases;

import java.io.Serializable;

public class Cita implements Serializable {
    private String cedula;
    private String idMascota;
    private String fecha;

    public Cita(String cedula, String idMascota, String fecha) {
        this.cedula = cedula;
        this.idMascota = idMascota;
        this.fecha = fecha;
    }

    public String getCedula() { return cedula; }
    public String getIdMascota() { return idMascota; }
    public String getFecha() { return fecha; }

    @Override
    public String toString() {
        return "Cita para mascota ID: " + idMascota + " el " + fecha + " (Dueño: " + cedula + ")";
    }
}
