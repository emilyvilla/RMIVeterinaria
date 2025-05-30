/**
 *
 * @Emily Villa
 */
package clases;


public class Pago {
    public String cedula;
    public String concepto;
    public double monto;
    public String fecha; // Puedes usar LocalDate.now().toString()

    public Pago(String cedula, String concepto, double monto, String fecha) {
        this.cedula = cedula;
        this.concepto = concepto;
        this.monto = monto;
        this.fecha = fecha;
    }
}
