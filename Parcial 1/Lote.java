package co.edu.uniquindio.ecoruta;

// Clase abstracta que representa un lote de material. Cada tipo de lote
// sobrescribe calcularValor() y asi el total de la entrega se calcula
// de forma polimorfica.
public abstract class Lote {

    private final String codigo;
    private String descripcion;
    private final int pesoKg;

    public Lote(String codigo, String descripcion, int pesoKg) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.pesoKg = pesoKg;
    }

    // Valor en pesos que EcoRuta reconoce al aportante por este lote.
    public abstract long calcularValor();

    // Un lote es valido si tiene codigo y un peso entero positivo.
    // Las subclases agregan sus propias condiciones.
    public boolean esValido() {
        return codigo != null && !codigo.isBlank() && pesoKg > 0;
    }

    // El codigo y el peso no tienen setter: no deben cambiar despues de creado
    // el lote, porque el valor de la entrega depende de ellos.
    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPesoKg() {
        return pesoKg;
    }

    @Override
    public String toString() {
        return "Lote " + codigo + " (" + descripcion + ") - " + pesoKg + " kg";
    }
}
