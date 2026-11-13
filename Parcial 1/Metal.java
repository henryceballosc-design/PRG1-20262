package co.edu.uniquindio.ecoruta;

// Metales: el valor por kg depende del tipo (aluminio o acero).
public class Metal extends Lote implements Etiquetable {

    private final TipoMetal tipoMetal;

    public Metal(String codigo, String descripcion, int pesoKg, TipoMetal tipoMetal) {
        super(codigo, descripcion, pesoKg);
        this.tipoMetal = tipoMetal;
    }

    @Override
    public long calcularValor() {
        return tipoMetal.getValorPorKg() * getPesoKg();
    }

    @Override
    public boolean esValido() {
        return super.esValido() && tipoMetal != null;
    }

    @Override
    public String generarEtiqueta() {
        return "Lote " + getCodigo() + " | Metal: " + tipoMetal;
    }

    public TipoMetal getTipoMetal() {
        return tipoMetal;
    }
}
