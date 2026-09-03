import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MenuAgenda {

    private SistemaAgenda sistema;
    private BufferedReader lector;
    private DateTimeFormatter formatoFecha;
    private DateTimeFormatter formatoHora;
    private boolean modoPrueba;

    public MenuAgenda(SistemaAgenda sistema) {
        this.sistema = sistema;
        this.lector = new BufferedReader(new InputStreamReader(System.in));
        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        this.modoPrueba = false;
    }

    public void seleccionarModo() {
        try {
            System.out.println("===== MODO DE INICIO =====");
            System.out.println("1. Modo normal");
            System.out.println("2. Modo prueba");
            System.out.print("Seleccione una opcion: ");

            String opcion = lector.readLine();

            if (opcion.equals("2")) {
                modoPrueba = true;
                sistema.cargarDatosIniciales();
                System.out.println("Modo prueba iniciado.");
            } else {
                modoPrueba = false;
                System.out.println("Modo normal iniciado.");
            }

        } catch (IOException error) {
            System.out.println("No se pudo leer la opcion.");
        }
    }

    public void iniciar() {
        boolean continuar = true;

        while (continuar) {
            mostrarOpciones();

            try {
                String opcion = lector.readLine();

                if (opcion.equals("1")) {
                    agregarActividad();

                } else if (opcion.equals("2")) {
                    sistema.mostrarDias();

                } else if (opcion.equals("3")) {
                    continuar = false;
                    System.out.println("Programa finalizado.");

                } else {
                    System.out.println("Opcion no valida.");
                }

            } catch (IOException error) {
                System.out.println("No se pudo leer la opcion ingresada.");
            }
        }
    }

    public void mostrarOpciones() {
        System.out.println();
        System.out.println("===== SISTEMA DE AGENDA =====");
        System.out.println("1. Agregar actividad");
        System.out.println("2. Mostrar agenda");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    public void agregarActividad() throws IOException {
        System.out.println();
        System.out.println("Tipo de actividad");
        System.out.println("1. Academica");
        System.out.println("2. Proyecto");
        System.out.println("3. Personal");
        System.out.println("4. Otro");

        System.out.print("Seleccione un tipo: ");

        String tipo = lector.readLine();

        LocalDate fecha = leerFecha("Ingrese fecha (dd/MM/yyyy): ");
        String titulo = leerTexto("Ingrese titulo: ");
        LocalTime horaInicio = leerHora("Ingrese hora de inicio (HH:mm): ");
        LocalTime horaFin = leerHora("Ingrese hora de fin (HH:mm): ");
        String descripcion = leerTexto("Ingrese descripcion: ");

        int id = sistema.generarIdActividad();

        Actividad actividad = crearActividadPorTipo(
                id, tipo, titulo, horaInicio, horaFin, descripcion
        );

        sistema.agregarActividad(fecha, actividad);

        System.out.println("Actividad agregada correctamente.");
    }

    public Actividad crearActividadPorTipo(int id, String tipo, String titulo,
                                           LocalTime horaInicio, LocalTime horaFin,
                                           String descripcion) throws IOException {

        if (tipo.equals("1")) {
            String asignatura = leerTexto("Ingrese asignatura: ");

            return new ActividadAcademica(
                    id, titulo, horaInicio, horaFin,
                    descripcion, asignatura
            );
        }

        if (tipo.equals("2")) {
            String nombreProyecto = leerTexto("Ingrese nombre del proyecto: ");

            return new ActividadProyecto(
                    id, titulo, horaInicio, horaFin,
                    descripcion, nombreProyecto
            );
        }

        if (tipo.equals("3")) {
            String lugar = leerTexto("Ingrese lugar: ");

            return new ActividadPersonal(
                    id, titulo, horaInicio, horaFin,
                    descripcion, lugar
            );
        }

        if (tipo.equals("4")) {
            String lugar = leerTexto("Ingrese lugar: ");

            return new ActividadOtro(
                    id, titulo, horaInicio, horaFin,
                    descripcion, lugar
            );
        }

        System.out.println("Tipo no valido. Se agregara como otro.");
        String lugar = leerTexto("Ingrese lugar: ");

        return new ActividadOtro(
                id, titulo, horaInicio, horaFin,
                descripcion, lugar
        );
    }

    public String leerTexto(String mensaje) throws IOException {
        System.out.print(mensaje);
        String texto = lector.readLine();

        while (texto.trim().length() == 0) {
            System.out.println("El texto no puede estar vacio.");
            System.out.print(mensaje);
            texto = lector.readLine();
        }

        return texto;
    }

    public LocalDate leerFecha(String mensaje) throws IOException {
        boolean fechaValida = false;
        LocalDate fecha = null;

        while (!fechaValida) {
            System.out.print(mensaje);
            String texto = lector.readLine();

            try {
                fecha = LocalDate.parse(texto, formatoFecha);
                fechaValida = true;

            } catch (DateTimeParseException error) {
                System.out.println(
                        "Fecha invalida. Ejemplo valido: 23/08/2026"
                );
            }
        }

        return fecha;
    }

    public LocalTime leerHora(String mensaje) throws IOException {
        boolean horaValida = false;
        LocalTime hora = null;

        while (!horaValida) {
            System.out.print(mensaje);
            String texto = lector.readLine();

            try {
                hora = LocalTime.parse(texto, formatoHora);
                horaValida = true;

            } catch (DateTimeParseException error) {
                System.out.println(
                        "Hora invalida. Ejemplo valido: 14:30"
                );
            }
        }

        return hora;
    }
}
