package co.edu.uniquindio.devplus;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombreCompleto;
    private String documentoIdentidad;
    private String telefono;
    private String correoElectronico;
    private String pais;
    private List<Proyecto> proyectos;

    public Cliente(String nombreCompleto, String documentoIdentidad, String telefono,
                    String correoElectronico, String pais) {
        this.nombreCompleto = nombreCompleto;
        this.documentoIdentidad = documentoIdentidad;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.pais = pais;
        this.proyectos = new ArrayList<>();
    }

    public void agregarProyecto(Proyecto proyecto) {
        proyectos.add(proyecto);
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public String getTelefono() { return telefono; }
    public String getCorreoElectronico() { return correoElectronico; }
    public String getPais() { return pais; }
    public List<Proyecto> getProyectos() { return proyectos; }

    @Override
    public String toString() {
        return "Cliente{nombre='" + nombreCompleto + "', documento='" + documentoIdentidad +
                "', telefono='" + telefono + "', correo='" + correoElectronico +
                "', pais='" + pais + "'}";
    }
}
