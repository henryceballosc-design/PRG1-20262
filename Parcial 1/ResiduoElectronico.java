package co.edu.uniquindio.ecoruta;

// Residuos electronicos: $1.400 por kg, mas $800 por cada equipo completo.
public class ResiduoElectronico extends Lote implements Etiquetable {

    private static final long VALOR_POR_KG = 1400;
    private static final long VALOR_POR_EQUIPO = 800;

    private final int cantidadEquipos;

    public ResiduoElectronico(String codigo, String descripcion, int pesoKg, int cantidadEquipos) {
        super(codigo, descripcion, pesoKg);
        this.cantidadEquipos = cantidadEquipos;
    }

    @Override
    public long calcularValor() {
        return VALOR_POR_KG * getPesoKg() + VALOR_POR_EQUIPO * cantidadEquipos;
    }

    // Ademas de lo del Lote, la cantidad de equipos debe ser mayor o igual a cero.
    @Override
    public boolean esValido() {
        return super.esValido() && cantidadEquipos >= 0;
    }

    @Override
    public String generarEtiqueta() {
        return "Lote " + getCodigo() + " | Equipos completos: " + cantidadEquipos;
    }

    public int getCantidadEquipos() {
        return cantidadEquipos;
    }
}
