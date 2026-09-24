package co.edu.uniquindio.devplus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private String nombreComercial;
    private String nit;
    private String direccion;
    private String telefono;
    private String paginaWeb;
    private List<Cliente> clientes;
    private List<Desarrollador> desarrolladores;
    private List<Proyecto> proyectos;
    private List<ServicioAdicional> servicios;

    public Empresa(String nombreComercial, String nit, String direccion,
                    String telefono, String paginaWeb) {
        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.paginaWeb = paginaWeb;
        this.clientes = new ArrayList<>();
        this.desarrolladores = new ArrayList<>();
        this.proyectos = new ArrayList<>();
        this.servicios = new ArrayList<>();
    }

    public void registrarCliente(Cliente cliente) { clientes.add(cliente); }
    public void registrarDesarrollador(Desarrollador desarrollador) { desarrolladores.add(desarrollador); }
    public void registrarServicio(ServicioAdicional servicio) { servicios.add(servicio); }

    public void registrarProyecto(Proyecto proyecto) {
        proyectos.add(proyecto);
        proyecto.getCliente().agregarProyecto(proyecto);
    }

    public Cliente buscarClientePorTelefono(String telefono) {
        for (Cliente c : clientes) {
            if (c.getTelefono().equals(telefono)) return c;
        }
        return null;
    }

    public Desarrollador buscarDesarrolladorPorCodigo(String codigo) {
        for (Desarrollador d : desarrolladores) {
            if (d.getCodigo().equals(codigo)) return d;
        }
        return null;
    }

    public Proyecto buscarProyectoPorCodigo(String codigo) {
        for (Proyecto p : proyectos) {
            if (p.getCodigo().equals(codigo)) return p;
        }
        return null;
    }

    public ServicioAdicional buscarServicioPorCodigo(String codigo) {
        for (ServicioAdicional s : servicios) {
            if (s.getCodigo().equals(codigo)) return s;
        }
        return null;
    }

    // Un numero perfecto es igual a la suma de sus divisores propios (sin incluirse a si mismo)
    public static boolean esNumeroPerfecto(long numero) {
        if (numero <= 0) return false;
        long suma = 0;
        for (long i = 1; i < numero; i++) {
            if (numero % i == 0) {
                suma += i;
            }
        }
        return suma == numero;
    }

    public double consultarIngresosPorFecha(LocalDate fecha) {
        double total = 0;
        for (Proyecto p : proyectos) {
            if (p.getFechaSolicitud().equals(fecha)) {
                total += p.getValorTotal();
            }
        }
        return total;
    }

    public List<Cliente> getClientes() { return clientes; }
    public List<Desarrollador> getDesarrolladores() { return desarrolladores; }
    public List<Proyecto> getProyectos() { return proyectos; }
    public List<ServicioAdicional> getServicios() { return servicios; }
    public String getNombreComercial() { return nombreComercial; }
}
