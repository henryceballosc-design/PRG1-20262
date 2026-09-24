package co.edu.uniquindio.devplus;

public class Desarrollador {
    private String codigo;
    private String equipoTrabajo;
    private NivelDesarrollador nivel;
    private int cantidadMaximaProyectosSimultaneos;
    private double tarifaPorDia;
    private EstadoDesarrollador estado;
    private int proyectosAsignadosActualmente;

    public Desarrollador(String codigo, String equipoTrabajo, NivelDesarrollador nivel,
                          int cantidadMaximaProyectosSimultaneos, double tarifaPorDia) {
        this.codigo = codigo;
        this.equipoTrabajo = equipoTrabajo;
        this.nivel = nivel;
        this.cantidadMaximaProyectosSimultaneos = cantidadMaximaProyectosSimultaneos;
        this.tarifaPorDia = tarifaPorDia;
        this.estado = EstadoDesarrollador.DISPONIBLE;
        this.proyectosAsignadosActualmente = 0;
    }

    public boolean estaDisponible() {
        return estado != EstadoDesarrollador.EN_CAPACITACION
                && proyectosAsignadosActualmente < cantidadMaximaProyectosSimultaneos;
    }

    public void asignarAProyecto() {
        proyectosAsignadosActualmente++;
        actualizarEstadoPorCarga();
    }

    private void actualizarEstadoPorCarga() {
        if (proyectosAsignadosActualmente == 0) {
            estado = EstadoDesarrollador.DISPONIBLE;
        } else if (proyectosAsignadosActualmente >= cantidadMaximaProyectosSimultaneos) {
            estado = EstadoDesarrollador.OCUPADO;
        } else {
            estado = EstadoDesarrollador.ASIGNADO;
        }
    }

    public String getCodigo() { return codigo; }
    public String getEquipoTrabajo() { return equipoTrabajo; }
    public NivelDesarrollador getNivel() { return nivel; }
    public int getCantidadMaximaProyectosSimultaneos() { return cantidadMaximaProyectosSimultaneos; }
    public double getTarifaPorDia() { return tarifaPorDia; }
    public EstadoDesarrollador getEstado() { return estado; }
    public void setEstado(EstadoDesarrollador estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Desarrollador{codigo='" + codigo + "', nivel=" + nivel +
                ", tarifaPorDia=" + tarifaPorDia + ", estado=" + estado + "}";
    }
}
