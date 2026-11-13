package co.edu.uniquindio.ecoruta;

import java.time.LocalDate;

// Clase administradora: guarda los aportantes y las entregas del centro de
// aprovechamiento y concentra las operaciones y consultas del negocio.
public class EcoRuta {

    private static final int CAPACIDAD_APORTANTES = 100;
    private static final int CAPACIDAD_ENTREGAS = 200;

    private String nombre;

    // Arreglos de tamano fijo con contador (no se usa ArrayList en este curso).
    private final Aportante[] aportantes;
    private int cantidadAportantes;
    private final Entrega[] entregas;
    private int cantidadEntregas;

    public EcoRuta(String nombre) {
        this.nombre = nombre;
        this.aportantes = new Aportante[CAPACIDAD_APORTANTES];
        this.cantidadAportantes = 0;
        this.entregas = new Entrega[CAPACIDAD_ENTREGAS];
        this.cantidadEntregas = 0;
    }

    public boolean registrarAportante(Aportante aportante) {
        if (aportante == null || buscarAportante(aportante.identificacion()) != null
                || cantidadAportantes >= aportantes.length) {
            return false;
        }
        aportantes[cantidadAportantes] = aportante;
        cantidadAportantes++;
        return true;
    }

    public Aportante buscarAportante(String identificacion) {
        for (int i = 0; i < cantidadAportantes; i++) {
            if (aportantes[i].identificacion().equals(identificacion)) {
                return aportantes[i];
            }
        }
        return null;
    }

    // Crea una entrega ABIERTA y sin lotes. Se rechaza si el codigo ya existe
    // o si la identificacion del aportante es desconocida.
    public boolean crearEntrega(String codigo, LocalDate fecha, String identificacionAportante) {
        if (codigo == null || codigo.isBlank() || buscarEntrega(codigo) != null
                || cantidadEntregas >= entregas.length) {
            return false;
        }
        Aportante aportante = buscarAportante(identificacionAportante);
        if (aportante == null) {
            return false;
        }
        entregas[cantidadEntregas] = new Entrega(codigo, fecha, aportante);
        cantidadEntregas++;
        return true;
    }

    public Entrega buscarEntrega(String codigo) {
        for (int i = 0; i < cantidadEntregas; i++) {
            if (entregas[i].getCodigo().equals(codigo)) {
                return entregas[i];
            }
        }
        return null;
    }

    // Se rechaza si la entrega no existe, esta cerrada, el peso no es valido
    // o el codigo del lote ya esta en esa entrega.
    public boolean agregarLote(String codigoEntrega, Lote lote) {
        Entrega entrega = buscarEntrega(codigoEntrega);
        if (entrega == null) {
            return false;
        }
        return entrega.agregarLote(lote);
    }

    // Se rechaza si la entrega no existe, esta vacia o ya fue aceptada.
    // En esos casos la informacion registrada se conserva.
    public boolean aceptarEntrega(String codigoEntrega) {
        Entrega entrega = buscarEntrega(codigoEntrega);
        if (entrega == null) {
            return false;
        }
        return entrega.aceptar();
    }

    // Devuelve null si la entrega no existe o todavia no tiene comprobante.
    public Comprobante consultarComprobante(String codigoEntrega) {
        Entrega entrega = buscarEntrega(codigoEntrega);
        if (entrega == null) {
            return null;
        }
        return entrega.getComprobante();
    }

    // Devuelve null si la entrega no existe o no ha sido aceptada.
    public String[] consultarEtiquetas(String codigoEntrega) {
        Entrega entrega = buscarEntrega(codigoEntrega);
        if (entrega == null || entrega.getEstado() != EstadoEntrega.ACEPTADA) {
            return null;
        }
        return entrega.obtenerEtiquetas();
    }

    // Suma solo los comprobantes de entregas ACEPTADAS del aportante.
    // Si no hay comprobantes para esa identificacion, devuelve 0.
    public long consultarValorAcumulado(String identificacionAportante) {
        long total = 0;
        for (int i = 0; i < cantidadEntregas; i++) {
            Comprobante comprobante = entregas[i].getComprobante();
            if (entregas[i].getEstado() == EstadoEntrega.ACEPTADA && comprobante != null
                    && comprobante.identificacionAportante().equals(identificacionAportante)) {
                total += comprobante.valorTotal();
            }
        }
        return total;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadEntregas() {
        return cantidadEntregas;
    }

    public Aportante[] getAportantes() {
        Aportante[] copia = new Aportante[cantidadAportantes];
        for (int i = 0; i < cantidadAportantes; i++) {
            copia[i] = aportantes[i];
        }
        return copia;
    }

    public Entrega[] getEntregas() {
        Entrega[] copia = new Entrega[cantidadEntregas];
        for (int i = 0; i < cantidadEntregas; i++) {
            copia[i] = entregas[i];
        }
        return copia;
    }
}
