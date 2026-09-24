package co.edu.uniquindio.devplus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Proyecto {
    private String codigo;
    private LocalDate fechaSolicitud;
    private LocalDate fechaInicio;
    private LocalDate fechaEntrega;
    private EstadoProyecto estado;
    private MetodoPago metodoPago;
    private double valorTotal;
    private double descuento; // 0.0 a 1.0, ej. 0.1 = 10%
    private Cliente cliente;
    private List<Desarrollador> desarrolladores;
    private List<ServicioAdicional> servicios;

    public Proyecto(String codigo, LocalDate fechaSolicitud, LocalDate fechaInicio,
                     LocalDate fechaEntrega, MetodoPago metodoPago, Cliente cliente, double descuento) {
        this.codigo = codigo;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaInicio = fechaInicio;
        this.fechaEntrega = fechaEntrega;
        this.estado = EstadoProyecto.PENDIENTE;
        this.metodoPago = metodoPago;
        this.cliente = cliente;
        this.descuento = descuento;
        this.desarrolladores = new ArrayList<>();
        this.servicios = new ArrayList<>();
        this.valorTotal = 0.0;
    }

    public boolean agregarDesarrollador(Desarrollador desarrollador) {
        if (!desarrollador.estaDisponible()) {
            return false;
        }
        desarrolladores.add(desarrollador);
        desarrollador.asignarAProyecto();
        calcularValorTotal();
        return true;
    }

    public void agregarServicio(ServicioAdicional servicio) {
        if (servicio.isDisponibilidad()) {
            servicios.add(servicio);
            calcularValorTotal();
        }
    }

    public long getCantidadDias() {
        if (fechaInicio == null || fechaEntrega == null) return 0;
        return Math.max(ChronoUnit.DAYS.between(fechaInicio, fechaEntrega), 1);
    }

    public void calcularValorTotal() {
        double totalDesarrolladores = 0;
        for (Desarrollador d : desarrolladores) {
            totalDesarrolladores += d.getTarifaPorDia() * getCantidadDias();
        }
        double totalServicios = 0;
        for (ServicioAdicional s : servicios) {
            totalServicios += s.getPrecio();
        }
        double subtotal = totalDesarrolladores + totalServicios;
        this.valorTotal = subtotal - (subtotal * descuento);
    }

    public void confirmar() {
        this.estado = EstadoProyecto.CONFIRMADO;
        for (Desarrollador d : desarrolladores) {
            d.setEstado(EstadoDesarrollador.ASIGNADO);
        }
    }

    public void cambiarEstado(EstadoProyecto nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String getCodigo() { return codigo; }
    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public EstadoProyecto getEstado() { return estado; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public double getValorTotal() { return valorTotal; }
    public Cliente getCliente() { return cliente; }
    public List<Desarrollador> getDesarrolladores() { return desarrolladores; }
    public List<ServicioAdicional> getServicios() { return servicios; }

    @Override
    public String toString() {
        return "Proyecto{codigo='" + codigo + "', estado=" + estado +
                ", valorTotal=" + valorTotal + ", cliente=" + cliente.getNombreCompleto() + "}";
    }
}
