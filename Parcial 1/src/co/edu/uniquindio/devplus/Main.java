package co.edu.uniquindio.devplus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Empresa empresa = new Empresa("DevPlus", "900123456-1",
            "Calle 10 # 5-20", "6067359300", "www.devplus.com");
    static DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        cargarDatosDePrueba();
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1 -> registrarCliente();
                case 2 -> registrarDesarrollador();
                case 3 -> registrarServicio();
                case 4 -> registrarProyecto();
                case 5 -> asignarDesarrolladorAProyecto();
                case 6 -> agregarServicioAProyecto();
                case 7 -> confirmarProyecto();
                case 8 -> consultarClientePorTelefono();
                case 9 -> consultarIngresosPorFecha();
                case 10 -> listarProyectos();
                case 0 -> System.out.println("Saliendo del sistema DevPlus...");
                default -> System.out.println("Opcion no valida.");
            }
            System.out.println();
        } while (opcion != 0);
        sc.close();
    }

    static void mostrarMenu() {
        System.out.println("===== Sistema DevPlus =====");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Registrar desarrollador");
        System.out.println("3. Registrar servicio adicional");
        System.out.println("4. Registrar proyecto");
        System.out.println("5. Asignar desarrollador a proyecto");
        System.out.println("6. Agregar servicio a proyecto");
        System.out.println("7. Confirmar proyecto");
        System.out.println("8. Consultar cliente por telefono (numero perfecto)");
        System.out.println("9. Consultar ingresos por fecha de solicitud");
        System.out.println("10. Listar proyectos registrados");
        System.out.println("0. Salir");
    }

    static void registrarCliente() {
        System.out.println("--- Registrar cliente ---");
        String nombre = leerTexto("Nombre completo o razon social: ");
        String documento = leerTexto("Documento o NIT: ");
        String telefono = leerTexto("Telefono: ");
        String correo = leerTexto("Correo electronico: ");
        String pais = leerTexto("Pais: ");
        empresa.registrarCliente(new Cliente(nombre, documento, telefono, correo, pais));
        System.out.println("Cliente registrado correctamente.");
    }

    static void registrarDesarrollador() {
        System.out.println("--- Registrar desarrollador ---");
        String codigo = leerTexto("Codigo: ");
        String equipo = leerTexto("Equipo de trabajo: ");
        NivelDesarrollador nivel = leerNivel();
        int maxProyectos = leerEntero("Cantidad maxima de proyectos simultaneos: ");
        double tarifa = leerDecimal("Tarifa por dia: ");
        empresa.registrarDesarrollador(new Desarrollador(codigo, equipo, nivel, maxProyectos, tarifa));
        System.out.println("Desarrollador registrado correctamente.");
    }

    static void registrarServicio() {
        System.out.println("--- Registrar servicio adicional ---");
        String codigo = leerTexto("Codigo: ");
        String nombre = leerTexto("Nombre: ");
        String descripcion = leerTexto("Descripcion: ");
        double precio = leerDecimal("Precio: ");
        empresa.registrarServicio(new ServicioAdicional(codigo, nombre, descripcion, precio, true));
        System.out.println("Servicio registrado correctamente.");
    }

    static void registrarProyecto() {
        System.out.println("--- Registrar proyecto ---");
        String codigo = leerTexto("Codigo del proyecto: ");
        String telefonoCliente = leerTexto("Telefono del cliente (ya registrado): ");
        Cliente cliente = empresa.buscarClientePorTelefono(telefonoCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado. Registre el cliente primero.");
            return;
        }
        LocalDate fechaSolicitud = leerFecha("Fecha de solicitud (yyyy-MM-dd): ");
        LocalDate fechaInicio = leerFecha("Fecha de inicio (yyyy-MM-dd): ");
        LocalDate fechaEntrega = leerFecha("Fecha de entrega (yyyy-MM-dd): ");
        MetodoPago metodoPago = leerMetodoPago();
        double descuento = leerDecimal("Descuento (0.0 a 1.0, ej 0.1 = 10%): ");
        Proyecto proyecto = new Proyecto(codigo, fechaSolicitud, fechaInicio, fechaEntrega,
                metodoPago, cliente, descuento);
        empresa.registrarProyecto(proyecto);
        System.out.println("Proyecto registrado correctamente.");
    }

    static void asignarDesarrolladorAProyecto() {
        System.out.println("--- Asignar desarrollador a proyecto ---");
        Proyecto proyecto = empresa.buscarProyectoPorCodigo(leerTexto("Codigo del proyecto: "));
        if (proyecto == null) { System.out.println("Proyecto no encontrado."); return; }
        Desarrollador desarrollador = empresa.buscarDesarrolladorPorCodigo(leerTexto("Codigo del desarrollador: "));
        if (desarrollador == null) { System.out.println("Desarrollador no encontrado."); return; }
        boolean asignado = proyecto.agregarDesarrollador(desarrollador);
        if (asignado) {
            System.out.println("Desarrollador asignado. Nuevo valor total: " + proyecto.getValorTotal());
        } else {
            System.out.println("El desarrollador no esta disponible.");
        }
    }

    static void agregarServicioAProyecto() {
        System.out.println("--- Agregar servicio a proyecto ---");
        Proyecto proyecto = empresa.buscarProyectoPorCodigo(leerTexto("Codigo del proyecto: "));
        if (proyecto == null) { System.out.println("Proyecto no encontrado."); return; }
        ServicioAdicional servicio = empresa.buscarServicioPorCodigo(leerTexto("Codigo del servicio: "));
        if (servicio == null) { System.out.println("Servicio no encontrado."); return; }
        proyecto.agregarServicio(servicio);
        System.out.println("Servicio agregado. Nuevo valor total: " + proyecto.getValorTotal());
    }

    static void confirmarProyecto() {
        System.out.println("--- Confirmar proyecto ---");
        Proyecto proyecto = empresa.buscarProyectoPorCodigo(leerTexto("Codigo del proyecto: "));
        if (proyecto == null) { System.out.println("Proyecto no encontrado."); return; }
        proyecto.confirmar();
        System.out.println("Proyecto confirmado. Disponibilidad de desarrolladores actualizada.");
    }

    static void consultarClientePorTelefono() {
        System.out.println("--- Consultar cliente por telefono ---");
        String telefono = leerTexto("Telefono: ");
        Cliente cliente = empresa.buscarClientePorTelefono(telefono);
        if (cliente == null) { System.out.println("Cliente no encontrado."); return; }
        System.out.println(cliente);
        try {
            long numero = Long.parseLong(telefono.replaceAll("[^0-9]", ""));
            boolean esPerfecto = Empresa.esNumeroPerfecto(numero);
            System.out.println("El telefono " + (esPerfecto ? "SI" : "NO") + " corresponde a un numero perfecto.");
        } catch (NumberFormatException e) {
            System.out.println("El telefono no se puede evaluar como numero perfecto.");
        }
    }

    static void consultarIngresosPorFecha() {
        System.out.println("--- Consultar ingresos por fecha de solicitud ---");
        LocalDate fecha = leerFecha("Fecha de solicitud a consultar (yyyy-MM-dd): ");
        double total = empresa.consultarIngresosPorFecha(fecha);
        System.out.println("Ingresos totales para " + fecha + ": " + total);
    }

    static void listarProyectos() {
        System.out.println("--- Proyectos registrados ---");
        List<Proyecto> proyectos = empresa.getProyectos();
        if (proyectos.isEmpty()) { System.out.println("No hay proyectos registrados."); return; }
        for (Proyecto p : proyectos) {
            System.out.println(p);
        }
    }

    // ---------- Datos de prueba para agilizar la demostracion ----------
    static void cargarDatosDePrueba() {
        Cliente c1 = new Cliente("Ana Torres", "CC1001", "3001112233", "ana@mail.com", "Colombia");
        empresa.registrarCliente(c1);
        empresa.registrarDesarrollador(new Desarrollador("D1", "Backend", NivelDesarrollador.SENIOR, 2, 150000));
        empresa.registrarDesarrollador(new Desarrollador("D2", "Frontend", NivelDesarrollador.JUNIOR, 3, 80000));
        empresa.registrarServicio(new ServicioAdicional("S1", "Soporte tecnico", "Soporte 24/7", 200000, true));
    }

    // ---------- Utilidades de lectura ----------
    static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un numero valido: ");
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine();
        return valor;
    }

    static double leerDecimal(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextDouble()) {
            System.out.print("Ingrese un valor decimal valido: ");
            sc.next();
        }
        double valor = sc.nextDouble();
        sc.nextLine();
        return valor;
    }

    static LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return LocalDate.parse(sc.nextLine(), formatoFecha);
            } catch (Exception e) {
                System.out.println("Formato invalido, use yyyy-MM-dd.");
            }
        }
    }

    static NivelDesarrollador leerNivel() {
        System.out.println("Nivel: 1) Junior 2) Semisenior 3) Senior");
        int op = leerEntero("Opcion: ");
        return switch (op) {
            case 1 -> NivelDesarrollador.JUNIOR;
            case 2 -> NivelDesarrollador.SEMISENIOR;
            default -> NivelDesarrollador.SENIOR;
        };
    }

    static MetodoPago leerMetodoPago() {
        System.out.println("Metodo de pago: 1) Tarjeta credito 2) Transferencia 3) Efectivo");
        int op = leerEntero("Opcion: ");
        return switch (op) {
            case 1 -> MetodoPago.TARJETA_CREDITO;
            case 2 -> MetodoPago.TRANSFERENCIA_BANCARIA;
            default -> MetodoPago.EFECTIVO;
        };
    }
}
