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

    public MenuAgenda(SistemaAgenda sistema) {
        this.sistema = sistema;
        this.lector = new BufferedReader(new InputStreamReader(System.in));
        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm");
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
                    buscarDia();
                } else if (opcion.equals("4")) {
                    editarDia();
                } else if (opcion.equals("5")) {
                    eliminarDia();
                } else if (opcion.equals("6")) {
                    buscarActividad();
                } else if (opcion.equals("7")) {
                    editarActividad();
                } else if (opcion.equals("8")) {
                    eliminarActividad();
                } else if (opcion.equals("9")) {
                    buscarHorarioDisponible();
                } else if (opcion.equals("10")) {
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
        System.out.println("3. Buscar dia");
        System.out.println("4. Editar dia");
        System.out.println("5. Eliminar dia");
        System.out.println("6. Buscar actividad");
        System.out.println("7. Editar actividad");
        System.out.println("8. Eliminar actividad");
        System.out.println("9. Buscar horario disponible");
        System.out.println("10. Salir");
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
        Actividad actividad = crearActividadPorTipo(id, tipo, titulo, horaInicio, horaFin, descripcion);

        sistema.agregarActividad(fecha, actividad);

        System.out.println("Actividad agregada correctamente.");
    }

    public Actividad crearActividadPorTipo(int id, String tipo, String titulo,
                                           LocalTime horaInicio, LocalTime horaFin,
                                           String descripcion) throws IOException {

        if (tipo.equals("1")) {
            String asignatura = leerTexto("Ingrese asignatura: ");
            return new ActividadAcademica(id, titulo, horaInicio, horaFin, descripcion, asignatura);
        }

        if (tipo.equals("2")) {
            String nombreProyecto = leerTexto("Ingrese nombre del proyecto: ");
            return new ActividadProyecto(id, titulo, horaInicio, horaFin, descripcion, nombreProyecto);
        }

        if (tipo.equals("3")) {
            String lugar = leerTexto("Ingrese lugar: ");
            return new ActividadPersonal(id, titulo, horaInicio, horaFin, descripcion, lugar);
        }

        if (tipo.equals("4")) {
            String lugar = leerTexto("Ingrese lugar: ");
            return new ActividadOtro(id, titulo, horaInicio, horaFin, descripcion, lugar);
        }

        System.out.println("Tipo no valido. Se agregara como otro.");
        String lugar = leerTexto("Ingrese lugar: ");
        return new ActividadOtro(id, titulo, horaInicio, horaFin, descripcion, lugar);
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
                System.out.println("Fecha invalida. Ejemplo valido: 23/08/2026");
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
                System.out.println("Hora invalida. Ejemplo valido: 14:30");
            }
        }

        return hora;
    }

public void buscarHorarioDisponible() throws IOException {
        System.out.println();
        System.out.println("----- BUSCAR HORARIO DISPONIBLE -----");
        LocalDate fecha = leerFecha("Ingrese fecha (dd/MM/yyyy): ");

        int duracionMinutos = 0;
        while (duracionMinutos <= 0 || duracionMinutos > 1440) {
            String duracionTexto = leerTexto("Ingrese duracion (en minutos): ");
            try {
                duracionMinutos = Integer.parseInt(duracionTexto);
                if (duracionMinutos <= 0 || duracionMinutos > 1440) {
                    System.out.println("La duracion debe ser un numero positivo y no mayor a 1440 minutos.");
                }
            } catch (NumberFormatException error) {
                System.out.println("Duracion invalida. Ingrese un numero entero.");
            }
        }
        
        String resultado = sistema.buscarHorarioDisponible(fecha, duracionMinutos);
        System.out.println(resultado);
    }

    public void buscarDia() throws IOException {
        System.out.println();
        System.out.println("----- BUSCAR DIA -----");
        LocalDate fecha = leerFecha("Ingrese fecha (dd/MM/yyyy): ");

        DiaAgenda dia = sistema.getDias().get(fecha);
        if (dia != null) {
            System.out.println("Fecha: " + fecha.format(formatoFecha) + ":");
            dia.mostrarActividades();
        } else {
            System.out.println("No se encontró el dia solicitado");
        }
    }

    public void editarDia() throws IOException {
        System.out.println();
        System.out.println("----- EDITAR DIA -----");
        LocalDate fecha = leerFecha("Ingrese fecha del dia a editar (dd/MM/yyyy): ");
        LocalDate nuevaFecha = leerFecha("Ingrese nueva fecha (dd/MM/yyyy): ");

        boolean editado = sistema.editarFechaDia(fecha, nuevaFecha);
        if (editado) {
            System.out.println("Fecha del dia editada correctamente.");
        } else {
            System.out.println("ERROR: No se pudo editar la fecha del dia.");
        }
    }

    public void eliminarDia() throws IOException {
        System.out.println();
        System.out.println("----- ELIMINAR DIA -----");
        LocalDate fecha = leerFecha("Ingrese fecha del dia a eliminar (dd/MM/yyyy): ");

        boolean eliminado = sistema.eliminarDia(fecha);
        if (eliminado) {
            System.out.println("Dia eliminado correctamente.");
        } else {
            System.out.println("ERROR: No se pudo eliminar el dia.");
        }
    }

    public void buscarActividad() throws IOException {
        System.out.println();
        System.out.println("----- BUSCAR ACTIVIDAD -----");
        //En este caso haré con id, pero posiblemente cambie en un futuro
        int id = leerEntero("Ingrese ID de la actividad: ");
        
        Actividad act = sistema.buscarActividadPorId(id);
        if (act != null) {
            System.out.println("Actividad encontrada:");
            System.out.println(act.mostrarActividad());
        } else {
            System.out.println("No se encontró la actividad con el ID especificado." + id);
        }
    }

public void editarActividad() throws IOException {
        System.out.println();
        System.out.println("----- EDITAR ACTIVIDAD -----");
        int id = leerEntero("Ingrese ID de la actividad a editar: ");
        
        Actividad act = sistema.buscarActividadPorId(id);
        if (act == null) {
            System.out.println("No se encontró la actividad con el ID especificado: " + id);
            return;
        }

        System.out.println("Actividad encontrada: " + act.mostrarActividad());
        String nuevoTitulo = leerTexto("Ingrese nuevo titulo: ");
        LocalTime nuevaHoraInicio = leerHora("Ingrese nueva hora de inicio (HH:mm): ");
        LocalTime nuevaHoraFin = leerHora("Ingrese nueva hora de fin (HH:mm): ");
        String nuevaDescripcion = leerTexto("Ingrese nueva descripcion: ");

        act.setTitulo(nuevoTitulo);
        act.setHoraInicio(nuevaHoraInicio);
        act.setHoraFin(nuevaHoraFin);
        act.setDescripcion(nuevaDescripcion);
        System.out.println("Actividad editada correctamente.");
    }

    public void eliminarActividad() throws IOException {
        System.out.println();
        System.out.println("----- ELIMINAR ACTIVIDAD -----");
        int id = leerEntero("Ingrese ID de la actividad a eliminar: ");
        
        boolean eliminada = sistema.eliminarActividadPorId(id);
        if (eliminada) {
            System.out.println("Actividad eliminada correctamente.");
        } else {
            System.out.println("No se pudo encontrar ninguna actividad con el ID especificado: " + id);
        }
    }

    public int leerEntero(String mensaje) throws IOException {
        while(true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException error) {
                System.out.println("Entrada invalida. Ingrese un numero entero.");
            }
        }
    }
}


