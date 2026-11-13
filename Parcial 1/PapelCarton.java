package co.edu.uniquindio.ecoruta;

// Papel y carton: $500 por kg, mas $100 por kg si esta clasificado.
// Se procesa sin etiqueta (no implementa Etiquetable).
public class PapelCarton extends Lote {

    private static final long VALOR_POR_KG = 500;
    private static final long BONO_CLASIFICADO_POR_KG = 100;

    private final boolean clasificado;

    public PapelCarton(String codigo, String descripcion, int pesoKg, boolean clasificado) {
        super(codigo, descripcion, pesoKg);
        this.clasificado = clasificado;
    }

    @Override
    public long calcularValor() {
        long valorPorKg = VALOR_POR_KG;
        if (clasificado) {
            valorPorKg += BONO_CLASIFICADO_POR_KG;
        }
        return valorPorKg * getPesoKg();
    }

    public boolean isClasificado() {
        return clasificado;
    }
}
