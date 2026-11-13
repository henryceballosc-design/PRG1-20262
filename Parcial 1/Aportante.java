package co.edu.uniquindio.ecoruta;

// Persona o entidad que entrega materiales. Es un record porque sus datos
// no se modifican despues de ser creados.
public record Aportante(String identificacion, String nombre, String correo) {
}
