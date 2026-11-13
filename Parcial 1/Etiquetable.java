package co.edu.uniquindio.ecoruta;

// Contrato para los lotes que generan etiqueta de control interno
// (metales y residuos electronicos). El papel y carton no la implementa.
public interface Etiquetable {

    String generarEtiqueta();
}
