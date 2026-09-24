package co.edu.uniquindio.devplus;

public class ServicioAdicional {
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private boolean disponibilidad;

    public ServicioAdicional(String codigo, String nombre, String descripcion,
                              double precio, boolean disponibilidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public boolean isDisponibilidad() { return disponibilidad; }

    @Override
    public String toString() {
        return "ServicioAdicional{codigo='" + codigo + "', nombre='" + nombre +
                "', precio=" + precio + ", disponible=" + disponibilidad + "}";
    }
}
