package co.edu.uniquindio.ecoruta;

// Resultado de una prueba funcional: lo que se probo, lo esperado y lo obtenido.
public record ResultadoPrueba(String nombre, String entrada, String esperado,
                              String obtenido, boolean cumple) {
}
