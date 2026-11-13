package co.edu.uniquindio.ecoruta;

// Comprobante que se genera al aceptar una entrega. Es un record porque
// sus datos no se modifican una vez generado.
public record Comprobante(String codigoEntrega, String identificacionAportante,
                          int pesoTotal, long valorTotal) {
}
