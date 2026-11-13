package co.edu.uniquindio.ecoruta;

import java.time.LocalDate;
import java.util.Locale;

// Las 5 pruebas funcionales de EcoRuta. Cada prueba crea su propia instancia
// de EcoRuta con un aportante registrado, para que sean independientes.
public class PruebasFuncionales {

    private static final LocalDate FECHA = LocalDate.of(2026, 11, 10);

    public static ResultadoPrueba[] ejecutarTodas() {
        return new ResultadoPrueba[] {
            prueba1RegistroEntrega(),
            prueba2ValidacionLotes(),
            prueba3RechazoEntregaVacia(),
            prueba4AceptacionYCalculo(),
            prueba5ConsultaEtiquetas()
        };
    }

    // Crea EcoRuta con el aportante 1001 ya registrado y la entrega E01 abierta.
    private static EcoRuta crearEcoRutaConEntrega() {
        EcoRuta ecoRuta = new EcoRuta("EcoRuta");
        ecoRuta.registrarAportante(new Aportante("1001", "Ana Torres", "ana@mail.com"));
        ecoRuta.crearEntrega("E01", FECHA, "1001");
        return ecoRuta;
    }

    private static String pesos(long valor) {
        return String.format(Locale.forLanguageTag("es-CO"), "$%,d", valor);
    }

    // Prueba 1: registro de una entrega.
    private static ResultadoPrueba prueba1RegistroEntrega() {
        EcoRuta ecoRuta = new EcoRuta("EcoRuta");
        ecoRuta.registrarAportante(new Aportante("1001", "Ana Torres", "ana@mail.com"));

        boolean creada = ecoRuta.crearEntrega("E01", FECHA, "1001");
        Entrega e01 = ecoRuta.buscarEntrega("E01");
        boolean repetida = ecoRuta.crearEntrega("E01", FECHA, "1001");
        boolean desconocido = ecoRuta.crearEntrega("E02", FECHA, "9999");

        boolean cumple = creada && e01 != null && e01.getEstado() == EstadoEntrega.ABIERTA
                && e01.getCantidadLotes() == 0 && e01.getComprobante() == null
                && !repetida && !desconocido
                && ecoRuta.buscarEntrega("E01") == e01 && ecoRuta.getCantidadEntregas() == 1;

        String obtenido = "Crear E01: " + aceptadaRechazada(creada) + " (estado " + e01.getEstado()
                + ", " + e01.getCantidadLotes() + " lotes, comprobante "
                + (e01.getComprobante() == null ? "ninguno" : "existe") + "). "
                + "E01 repetida: " + aceptadaRechazada(repetida) + ". "
                + "E02 con aportante 9999: " + aceptadaRechazada(desconocido) + ". "
                + "Entregas registradas: " + ecoRuta.getCantidadEntregas() + " (E01 se conserva).";

        return new ResultadoPrueba("Prueba 1 - Registro de una entrega",
                "Aportante 1001 registrado. crearEntrega(E01, 1001); crearEntrega(E01, 1001); "
                        + "crearEntrega(E02, aportante 9999)",
                "E01 ABIERTA, sin lotes y sin comprobante. Las otras dos operaciones se rechazan "
                        + "y E01 se conserva.",
                obtenido, cumple);
    }

    // Prueba 2: validacion de lotes.
    private static ResultadoPrueba prueba2ValidacionLotes() {
        EcoRuta ecoRuta = crearEcoRutaConEntrega();

        boolean l01 = ecoRuta.agregarLote("E01", new PapelCarton("L01", "Papel", 5, false));
        boolean repetido = ecoRuta.agregarLote("E01", new PapelCarton("L01", "Papel repetido", 3, false));
        boolean sinPeso = ecoRuta.agregarLote("E01", new PapelCarton("L02", "Papel sin peso", 0, false));

        Entrega e01 = ecoRuta.buscarEntrega("E01");
        boolean cumple = l01 && !repetido && !sinPeso && e01.getCantidadLotes() == 1
                && e01.buscarLote("L01") != null && e01.buscarLote("L01").getPesoKg() == 5
                && e01.buscarLote("L02") == null;

        String obtenido = "Lote L01 de 5 kg: " + aceptadaRechazada(l01) + ". "
                + "Otro lote L01: " + aceptadaRechazada(repetido) + ". "
                + "Lote L02 de 0 kg: " + aceptadaRechazada(sinPeso) + ". "
                + "Lotes en E01: " + e01.getCantidadLotes() + " (solo L01 de 5 kg).";

        return new ResultadoPrueba("Prueba 2 - Validacion de lotes",
                "E01 abierta. agregarLote(L01, 5 kg); agregarLote(L01, 3 kg); agregarLote(L02, 0 kg)",
                "Se acepta L01. El lote con codigo repetido y el lote de 0 kg se rechazan. "
                        + "E01 conserva unicamente L01.",
                obtenido, cumple);
    }

    // Prueba 3: rechazo de una entrega vacia.
    private static ResultadoPrueba prueba3RechazoEntregaVacia() {
        EcoRuta ecoRuta = crearEcoRutaConEntrega();

        boolean aceptada = ecoRuta.aceptarEntrega("E01");
        Entrega e01 = ecoRuta.buscarEntrega("E01");

        boolean cumple = !aceptada && e01.getEstado() == EstadoEntrega.ABIERTA
                && e01.getComprobante() == null && ecoRuta.consultarComprobante("E01") == null;

        String obtenido = "Aceptar E01 sin lotes: " + aceptadaRechazada(aceptada) + ". Estado: "
                + e01.getEstado() + ". Comprobante: "
                + (e01.getComprobante() == null ? "ninguno" : "existe") + ".";

        return new ResultadoPrueba("Prueba 3 - Rechazo de una entrega vacia",
                "E01 abierta y sin lotes. aceptarEntrega(E01)",
                "La aceptacion se rechaza, E01 sigue ABIERTA y sin comprobante.",
                obtenido, cumple);
    }

    // Agrega los tres lotes de la prueba 4 y acepta la entrega E01.
    private static boolean cargarTresLotesYAceptar(EcoRuta ecoRuta) {
        ecoRuta.agregarLote("E01", new PapelCarton("L01", "Papel y carton clasificado", 10, true));
        ecoRuta.agregarLote("E01", new Metal("L02", "Aluminio", 5, TipoMetal.ALUMINIO));
        ecoRuta.agregarLote("E01", new ResiduoElectronico("L03", "Residuos electronicos", 4, 2));
        return ecoRuta.aceptarEntrega("E01");
    }

    // Prueba 4: aceptacion y calculo con los tres tipos de lote.
    private static ResultadoPrueba prueba4AceptacionYCalculo() {
        EcoRuta ecoRuta = crearEcoRutaConEntrega();
        boolean aceptada = cargarTresLotesYAceptar(ecoRuta);

        Entrega e01 = ecoRuta.buscarEntrega("E01");
        long v1 = e01.buscarLote("L01").calcularValor();
        long v2 = e01.buscarLote("L02").calcularValor();
        long v3 = e01.buscarLote("L03").calcularValor();
        Comprobante comprobante = ecoRuta.consultarComprobante("E01");

        boolean cumple = aceptada && e01.getEstado() == EstadoEntrega.ACEPTADA && comprobante != null
                && comprobante.pesoTotal() == 19 && comprobante.valorTotal() == 24200
                && v1 == 6000 && v2 == 11000 && v3 == 7200;

        String obtenido = "Aceptar E01: " + aceptadaRechazada(aceptada) + ". Estado: " + e01.getEstado()
                + ". Valores por lote: " + pesos(v1) + ", " + pesos(v2) + " y " + pesos(v3) + ". "
                + (comprobante == null ? "Sin comprobante."
                        : "Comprobante: " + comprobante.pesoTotal() + " kg y "
                                + pesos(comprobante.valorTotal()) + ".");

        return new ResultadoPrueba("Prueba 4 - Aceptacion y calculo con los tres tipos de lote",
                "E01 con L01 papel y carton clasificado 10 kg, L02 aluminio 5 kg y L03 residuos "
                        + "electronicos 4 kg con 2 equipos completos. aceptarEntrega(E01)",
                "E01 ACEPTADA. Valores por lote $6.000, $11.000 y $7.200. Comprobante: 19 kg y $24.200.",
                obtenido, cumple);
    }

    // Prueba 5: consulta de etiquetas de la entrega aceptada.
    private static ResultadoPrueba prueba5ConsultaEtiquetas() {
        EcoRuta ecoRuta = crearEcoRutaConEntrega();
        cargarTresLotesYAceptar(ecoRuta);

        String[] etiquetas = ecoRuta.consultarEtiquetas("E01");

        boolean cumple = etiquetas != null && etiquetas.length == 2
                && etiquetas[0].contains("L02") && etiquetas[0].contains("ALUMINIO")
                && etiquetas[1].contains("L03") && etiquetas[1].contains("2");

        StringBuilder obtenido = new StringBuilder();
        if (etiquetas == null) {
            obtenido.append("No se obtuvieron etiquetas.");
        } else {
            obtenido.append(etiquetas.length).append(" etiquetas:");
            for (String etiqueta : etiquetas) {
                obtenido.append(" \"").append(etiqueta).append("\".");
            }
        }

        return new ResultadoPrueba("Prueba 5 - Consulta de etiquetas",
                "Entrega E01 aceptada de la prueba 4. consultarEtiquetas(E01)",
                "Exactamente dos etiquetas: la del metal con su codigo y ALUMINIO, y la del lote "
                        + "electronico con su codigo y 2 equipos completos.",
                obtenido.toString(), cumple);
    }

    private static String aceptadaRechazada(boolean resultado) {
        return resultado ? "aceptada" : "rechazada";
    }
}
