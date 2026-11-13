package co.edu.uniquindio.ecoruta;

import java.time.LocalDate;

// Una entrega de materiales hecha por un aportante. Contiene lotes y, al
// ser aceptada, genera un comprobante.
public class Entrega {

    private static final int CAPACIDAD_LOTES = 50;

    private final String codigo;
    private final LocalDate fecha;
    private final Aportante aportante;
    private EstadoEntrega estado;
    private Comprobante comprobante; // null hasta que la entrega es aceptada

    // Arreglo de tamano fijo con contador (no se usa ArrayList en este curso).
    private final Lote[] lotes;
    private int cantidadLotes;

    public Entrega(String codigo, LocalDate fecha, Aportante aportante) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.aportante = aportante;
        this.estado = EstadoEntrega.ABIERTA;
        this.comprobante = null;
        this.lotes = new Lote[CAPACIDAD_LOTES];
        this.cantidadLotes = 0;
    }

    // Agrega un lote solo si la entrega esta ABIERTA, el lote es valido y su
    // codigo no se repite dentro de esta entrega. Si algo falla no cambia nada.
    public boolean agregarLote(Lote lote) {
        if (estado != EstadoEntrega.ABIERTA || lote == null || !lote.esValido()) {
            return false;
        }
        if (buscarLote(lote.getCodigo()) != null || cantidadLotes >= lotes.length) {
            return false;
        }
        lotes[cantidadLotes] = lote;
        cantidadLotes++;
        return true;
    }

    public Lote buscarLote(String codigoLote) {
        for (int i = 0; i < cantidadLotes; i++) {
            if (lotes[i].getCodigo().equals(codigoLote)) {
                return lotes[i];
            }
        }
        return null;
    }

    // Acepta la entrega: debe estar ABIERTA y tener al menos un lote.
    // Genera el comprobante con el peso y valor totales y la cierra a nuevos lotes.
    public boolean aceptar() {
        if (estado != EstadoEntrega.ABIERTA || cantidadLotes == 0) {
            return false;
        }
        comprobante = new Comprobante(codigo, aportante.identificacion(),
                calcularPesoTotal(), calcularValorTotal());
        estado = EstadoEntrega.ACEPTADA;
        return true;
    }

    // Calculo polimorfico: cada lote responde con su propia version de calcularValor().
    public long calcularValorTotal() {
        long total = 0;
        for (int i = 0; i < cantidadLotes; i++) {
            total += lotes[i].calcularValor();
        }
        return total;
    }

    public int calcularPesoTotal() {
        int total = 0;
        for (int i = 0; i < cantidadLotes; i++) {
            total += lotes[i].getPesoKg();
        }
        return total;
    }

    // Etiquetas de los lotes que implementan Etiquetable (el papel y carton no).
    public String[] obtenerEtiquetas() {
        int cantidad = 0;
        for (int i = 0; i < cantidadLotes; i++) {
            if (lotes[i] instanceof Etiquetable) {
                cantidad++;
            }
        }
        String[] etiquetas = new String[cantidad];
        int posicion = 0;
        for (int i = 0; i < cantidadLotes; i++) {
            if (lotes[i] instanceof Etiquetable etiquetable) {
                etiquetas[posicion] = etiquetable.generarEtiqueta();
                posicion++;
            }
        }
        return etiquetas;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Aportante getAportante() {
        return aportante;
    }

    public EstadoEntrega getEstado() {
        return estado;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public int getCantidadLotes() {
        return cantidadLotes;
    }

    public Lote[] getLotes() {
        Lote[] copia = new Lote[cantidadLotes];
        for (int i = 0; i < cantidadLotes; i++) {
            copia[i] = lotes[i];
        }
        return copia;
    }

    @Override
    public String toString() {
        return "Entrega " + codigo + " | " + fecha + " | Aportante: " + aportante.identificacion()
                + " | Estado: " + estado + " | Lotes: " + cantidadLotes;
    }
}
