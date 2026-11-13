package co.edu.uniquindio.ecoruta;

// Tipos de metal que recibe EcoRuta, cada uno con su valor reconocido por kilogramo.
public enum TipoMetal {
    ALUMINIO(2200),
    ACERO(900);

    private final long valorPorKg;

    TipoMetal(long valorPorKg) {
        this.valorPorKg = valorPorKg;
    }

    public long getValorPorKg() {
        return valorPorKg;
    }
}
