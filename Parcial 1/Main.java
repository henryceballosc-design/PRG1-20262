package co.edu.uniquindio.ecoruta;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

// Menu interactivo. Esta clase solo pide datos y muestra resultados: toda la
// logica del negocio esta en EcoRuta, Entrega y los lotes.
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final EcoRuta ecoRuta = new EcoRuta("EcoRuta");

    public static void main(String[] args) {
        cargarAportantesIniciales();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            System.out.println();

            switch (opcion) {
                case 1 -> registrarAportante();
                case 2 -> crearEntrega();
                case 3 -> agregarLote();
                case 4 -> aceptarEntrega();
                case 5 -> consultarComprobante();
                case 6 -> consultarEtiquetas();
                case 7 -> consultarValorAcumulado();
                case 8 -> listarEntregas();
                case 9 -> ejecutarPruebas();
                case 0 -> System.out.println("Saliendo de EcoRuta...");
                default -> System.out.println("Opcion no valida.");
            }
            System.out.println();
        } while (opcion != 0);

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("===== EcoRuta - Recepcion de materiales reciclables =====");
        System.out.println("1. Registrar aportante");
        System.out.println("2. Crear entrega");
        System.out.println("3. Agregar lote a una entrega");
        System.out.println("4. Aceptar entrega");
        System.out.println("5. Consultar comprobante por codigo de entrega");
        System.out.println("6. Consultar etiquetas por codigo de entrega");
        System.out.println("7. Consultar valor acumulado por aportante");
        System.out.println("8. Listar entregas");
        System.out.println("9. Ejecutar pruebas funcionales (1 a 5)");
        System.out.println("0. Salir");
    }

    private static void registrarAportante() {
        System.out.println("--- Registrar aportante ---");
        String identificacion = leerTexto("Identificacion: ");
        String nombre = leerTexto("Nombre: ");
        String correo = leerTexto("Correo: ");

        if (ecoRuta.registrarAportante(new Aportante(identificacion, nombre, correo))) {
            System.out.println("Aportante registrado.");
        } else {
            System.out.println("Rechazado: la identificacion ya esta registrada.");
        }
    }

    private static void crearEntrega() {
        System.out.println("--- Crear entrega ---");
        String codigo = leerTexto("Codigo de la entrega: ");
        LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd): ");
        String identificacion = leerTexto("Identificacion del aportante: ");

        if (ecoRuta.crearEntrega(codigo, fecha, identificacion)) {
            System.out.println("Entrega creada en estado ABIERTA, sin lotes.");
        } else {
            System.out.println("Rechazado: el codigo de entrega ya existe o el aportante es desconocido.");
        }
    }

    private static void agregarLote() {
        System.out.println("--- Agregar lote ---");
        String codigoEntrega = leerTexto("Codigo de la entrega: ");
        String codigoLote = leerTexto("Codigo del lote: ");
        String descripcion = leerTexto("Descripcion: ");
        int peso = leerEntero("Peso en kg: ");

        System.out.println("Tipo de lote: 1) Papel y carton  2) Metal  3) Residuo electronico");
        int tipo = leerEntero("Opcion: ");

        Lote lote;
        if (tipo == 1) {
            boolean clasificado = leerEntero("Clasificado antes de entregarse? 1) Si  2) No: ") == 1;
            lote = new PapelCarton(codigoLote, descripcion, peso, clasificado);
        } else if (tipo == 2) {
            TipoMetal tipoMetal = leerEntero("Metal: 1) Aluminio  2) Acero: ") == 1
                    ? TipoMetal.ALUMINIO : TipoMetal.ACERO;
            lote = new Metal(codigoLote, descripcion, peso, tipoMetal);
        } else if (tipo == 3) {
            int equipos = leerEntero("Cantidad de equipos completos: ");
            lote = new ResiduoElectronico(codigoLote, descripcion, peso, equipos);
        } else {
            System.out.println("Tipo de lote no valido.");
            return;
        }

        if (ecoRuta.agregarLote(codigoEntrega, lote)) {
            System.out.println("Lote agregado. Valor reconocido: " + pesos(lote.calcularValor()));
        } else {
            System.out.println("Rechazado: la entrega no existe o esta cerrada, el peso o la cantidad "
                    + "de equipos no es valido, o el codigo del lote esta repetido.");
        }
    }

    private static void aceptarEntrega() {
        System.out.println("--- Aceptar entrega ---");
        String codigo = leerTexto("Codigo de la entrega: ");

        if (ecoRuta.aceptarEntrega(codigo)) {
            System.out.println("Entrega aceptada. Se genero el comprobante.");
            imprimirComprobante(ecoRuta.consultarComprobante(codigo));
        } else {
            System.out.println("Rechazado: la entrega no existe, esta vacia o ya fue aceptada.");
        }
    }

    private static void consultarComprobante() {
        System.out.println("--- Consultar comprobante ---");
        Comprobante comprobante = ecoRuta.consultarComprobante(leerTexto("Codigo de la entrega: "));
        if (comprobante == null) {
            System.out.println("No hay comprobante: la entrega no existe o aun no fue aceptada.");
        } else {
            imprimirComprobante(comprobante);
        }
    }

    private static void consultarEtiquetas() {
        System.out.println("--- Consultar etiquetas ---");
        String[] etiquetas = ecoRuta.consultarEtiquetas(leerTexto("Codigo de la entrega: "));
        if (etiquetas == null) {
            System.out.println("No hay etiquetas: la entrega no existe o aun no fue aceptada.");
        } else if (etiquetas.length == 0) {
            System.out.println("La entrega no tiene lotes que generen etiqueta.");
        } else {
            for (String etiqueta : etiquetas) {
                System.out.println(etiqueta);
            }
        }
    }

    private static void consultarValorAcumulado() {
        System.out.println("--- Valor acumulado por aportante ---");
        String identificacion = leerTexto("Identificacion del aportante: ");
        System.out.println("Valor acumulado: " + pesos(ecoRuta.consultarValorAcumulado(identificacion)));
    }

    private static void listarEntregas() {
        System.out.println("--- Entregas registradas ---");
        Entrega[] entregas = ecoRuta.getEntregas();
        if (entregas.length == 0) {
            System.out.println("No hay entregas registradas.");
            return;
        }
        for (Entrega entrega : entregas) {
            System.out.println(entrega);
        }
    }

    private static void ejecutarPruebas() {
        System.out.println("--- Pruebas funcionales de EcoRuta ---");
        ResultadoPrueba[] resultados = PruebasFuncionales.ejecutarTodas();
        int cumplidas = 0;
        for (ResultadoPrueba r : resultados) {
            System.out.println(r.nombre());
            System.out.println("  Entrada:   " + r.entrada());
            System.out.println("  Esperado:  " + r.esperado());
            System.out.println("  Obtenido:  " + r.obtenido());
            System.out.println("  Resultado: " + (r.cumple() ? "CUMPLE" : "NO CUMPLE"));
            System.out.println();
            if (r.cumple()) {
                cumplidas++;
            }
        }
        System.out.println("Pruebas que cumplen: " + cumplidas + " de " + resultados.length);
    }

    private static void imprimirComprobante(Comprobante comprobante) {
        System.out.println("Comprobante de la entrega " + comprobante.codigoEntrega());
        System.out.println("  Aportante:   " + comprobante.identificacionAportante());
        System.out.println("  Peso total:  " + comprobante.pesoTotal() + " kg");
        System.out.println("  Valor total: " + pesos(comprobante.valorTotal()));
    }

    private static void cargarAportantesIniciales() {
        ecoRuta.registrarAportante(new Aportante("1001", "Ana Torres", "ana@mail.com"));
        ecoRuta.registrarAportante(new Aportante("1002", "Supermercado La Esperanza", "compras@laesperanza.com"));
        ecoRuta.registrarAportante(new Aportante("1003", "Colegio San Jose", "rectoria@sanjose.edu.co"));
    }

    private static String pesos(long valor) {
        return String.format(Locale.forLanguageTag("es-CO"), "$%,d", valor);
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido, use yyyy-MM-dd.");
            }
        }
    }
}
