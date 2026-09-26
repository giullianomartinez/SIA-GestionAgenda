import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MenuAgenda {

    private SistemaAgenda sistema;
    private BufferedReader lector;
    private DateTimeFormatter formatoFecha;
    private DateTimeFormatter formatoHora;

    public MenuAgenda(SistemaAgenda sistema, BufferedReader lector) {
        this.sistema = sistema;
        this.lector = lector;
        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm");
    }

    public SistemaAgenda getSistema() {
        return sistema;
    }

    public void setSistema(SistemaAgenda sistema) {
        this.sistema = sistema;
    }

    public BufferedReader getLector() {
        return lector;
    }

    public void setLector(BufferedReader lector) {
        this.lector = lector;
    }

    public DateTimeFormatter getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha(DateTimeFormatter formatoFecha) {
        this.formatoFecha = formatoFecha;
    }

    public DateTimeFormatter getFormatoHora() {
        return formatoHora;
    }

    public void setFormatoHora(DateTimeFormatter formatoHora) {
        this.formatoHora = formatoHora;
    }

    // Controla el menu de la agenda hasta volver al menu de inicio.
    public void iniciar() {

        boolean continuar = true;

        while (continuar) {

            mostrarOpciones();

            try {
                String opcion = lector.readLine();

                if (opcion.equals("1")) {
                    mostrarAgenda();

                } else if (opcion.equals("2")) {
                    sistema.mostrarHistorial();

                } else if (opcion.equals("3")) {
                    mostrarOpcionesActividades();

                } else if (opcion.equals("4")) {
                    mostrarOpcionesDias();

                } else if (opcion.equals("5")) {
                    buscarHorarioDisponible();

                } else if (opcion.equals("6")) {
                    continuar = false;

                } else {
                    System.out.println("\nOpcion no valida.");
                }

            } catch (IOException error) {
                System.out.println("\nNo se pudo leer la opcion ingresada.");
            }
        }
    }

    public void mostrarOpciones() {

        System.out.println();
        System.out.println("===== AGENDA =====");
        System.out.println("1. Mostrar agenda");
        System.out.println("2. Mostrar historial");
        System.out.println("3. Gestionar actividades");
        System.out.println("4. Gestionar dias");
        System.out.println("5. Buscar horario disponible");
        System.out.println("6. Volver al menu de inicio");

        System.out.print("Seleccione una opcion: ");
    }

    public void mostrarOpcionesActividades() throws IOException {

        boolean continuar = true;

        while (continuar) {

            System.out.println();
            System.out.println("===== ACTIVIDADES =====");
            System.out.println("1. Agregar actividad");
            System.out.println("2. Listar actividades de un dia");
            System.out.println("3. Buscar actividad");
            System.out.println("4. Mostrar actividades por tipo");
            System.out.println("5. Editar actividad");
            System.out.println("6. Eliminar actividad");
            System.out.println("7. Volver");

            System.out.print("Seleccione una opcion: ");
            String opcion = lector.readLine();

            if (opcion.equals("1")) {

                agregarActividad();

            } else if (opcion.equals("2")) {

                listarActividadesDeUnDia();

            } else if (opcion.equals("3")) {

                buscarActividad();

            } else if (opcion.equals("4")) {

                mostrarActividadesPorTipo();

            } else if (opcion.equals("5")) {

                editarActividad();

            } else if (opcion.equals("6")) {

                eliminarActividad();

            } else if (opcion.equals("7")) {

                continuar = false;

            } else {

                System.out.println("\nOpcion no valida.");
            }
        }
    }

    public void mostrarOpcionesDias() throws IOException {

        boolean continuar = true;

        while (continuar) {

            System.out.println();
            System.out.println("===== DIAS =====");
            System.out.println("1. Agregar dia");
            System.out.println("2. Listar dias");
            System.out.println("3. Buscar dia");
            System.out.println("4. Editar dia");
            System.out.println("5. Eliminar dia");
            System.out.println("6. Volver");

            System.out.print("Seleccione una opcion: ");
            String opcion = lector.readLine();

            if (opcion.equals("1")) {

                agregarDia();

            } else if (opcion.equals("2")) {

                sistema.listarDias();

            } else if (opcion.equals("3")) {

                buscarDia();

            } else if (opcion.equals("4")) {

                editarDia();

            } else if (opcion.equals("5")) {

                eliminarDia();

            } else if (opcion.equals("6")) {

                continuar = false;

            } else {

                System.out.println("\nOpcion no valida.");
            }
        }
    }

    public void agregarActividad() throws IOException {

        System.out.println();
        System.out.println("----- AGREGAR ACTIVIDAD -----");

        // Primero se solicita el dia
        LocalDate fecha = leerFecha("Ingrese fecha (dd/MM/yyyy): ");

        // Se verifica que el dia exista
        DiaAgenda dia = sistema.buscarDia(fecha);

        if (dia == null) {

            System.out.println("\nNo existe un dia registrado con esa fecha.");

            System.out.println("Primero debe agregar el dia.");

            return;
        }

        // Si el dia existe, continuamos con la actividad
        String tipo = leerTipoActividad();

        String titulo = leerTexto("Ingrese titulo: ");

        LocalTime horaInicio = leerHora("Ingrese hora de inicio (HH:mm): ");

        LocalTime horaFin = leerHoraFin(horaInicio);

        String descripcion = leerTexto("Ingrese descripcion: ");

        int id = sistema.generarIdActividad();

        try {

            Actividad actividad = crearActividadPorTipo(
                    id,
                    tipo,
                    titulo,
                    horaInicio,
                    horaFin,
                    descripcion
            );

            if (actividad == null) {
                System.out.println("\nNo se pudo crear la actividad.");
                return;
            }

            sistema.agregarActividad(fecha, actividad);

            System.out.println(
                    "\nActividad agregada correctamente."
            );

        } catch (HorarioInvalidoException error) {

            System.out.println(
                    "\nNo se pudo agregar la actividad: "
                            + error.getMessage()
            );
        }
    }

    public void buscarActividad() throws IOException {

        System.out.println();
        System.out.println("Buscar actividad por:");
        System.out.println("1. ID");
        System.out.println("2. Titulo");

        System.out.print("Seleccione una opcion: ");
        String opcion = lector.readLine();

        Actividad actividad = null;

        try {

            if (opcion.equals("1")) {

                int id = leerEntero("Ingrese ID de la actividad: ");
                actividad = sistema.buscarActividad(id);

            } else if (opcion.equals("2")) {

                String titulo = leerTexto("Ingrese titulo: ");
                actividad = sistema.buscarActividad(titulo);

            } else {

                System.out.println("\nOpcion no valida.");
                return;
            }

        } catch (ActividadNoEncontradaException error) {

            System.out.println("\n" + error.getMessage());

            return;
        }

        if (actividad == null) {
            System.out.println("\nActividad no encontrada.");

        } else {
            System.out.println("\nActividad encontrada:");
            System.out.println(actividad.mostrarActividad());
        }
    }

    public void mostrarAgenda() throws IOException {

        System.out.println();
        System.out.println("----- MOSTRAR AGENDA -----");
        System.out.println("1. Por dia");
        System.out.println("2. Por mes");
        System.out.println("3. Por anio");
        System.out.println("4. Agenda completa");

        System.out.print("Seleccione una opcion: ");
        String opcion = lector.readLine();

        if (opcion.equals("1")) {

            LocalDate fecha = leerFecha("Ingrese fecha (dd/MM/yyyy): ");
            sistema.mostrarDia(fecha);

        } else if (opcion.equals("2")) {

            int mes = leerMes();
            int anio = leerAnio();

            sistema.mostrarMes(mes, anio);

        } else if (opcion.equals("3")) {

            int anio = leerAnio();
            sistema.mostrarAnio(anio);

        } else if (opcion.equals("4")) {

            sistema.mostrarDias();

        } else {
            System.out.println("\nOpcion no valida.");
        }
    }

    public void mostrarActividadesPorTipo() throws IOException {

        LocalDate fecha = leerFecha("Ingrese fecha (dd/MM/yyyy): ");
        DiaAgenda dia = sistema.buscarDia(fecha);

        if (dia == null) {
            System.out.println("\nNo existe un dia registrado con esa fecha.");
            return;
        }

        System.out.println();
        System.out.println("Tipo de actividad:");
        System.out.println("1. Academica");
        System.out.println("2. Proyecto");
        System.out.println("3. Personal");
        System.out.println("4. Otro");

        System.out.print("Seleccione un tipo: ");
        String opcion = lector.readLine();

        String tipoActividad;

        if (opcion.equals("1")) {
            tipoActividad = "Academica";

        } else if (opcion.equals("2")) {
            tipoActividad = "Proyecto";

        } else if (opcion.equals("3")) {
            tipoActividad = "Personal";

        } else if (opcion.equals("4")) {
            tipoActividad = "Otro";

        } else {
            System.out.println("\nTipo no valido.");
            return;
        }

        dia.mostrarActividades(tipoActividad);
    }

    public void agregarDia() throws IOException {

        System.out.println();
        System.out.println("----- AGREGAR DIA -----");

        LocalDate fecha = leerFecha(
                "Ingrese fecha (dd/MM/yyyy): "
        );

        // Verificamos si el dia ya existe
        if (sistema.buscarDia(fecha) != null) {

            System.out.println(
                    "\nYa existe un dia registrado con esa fecha."
            );

            return;
        }

        // Creamos el objeto DiaAgenda
        DiaAgenda nuevoDia = new DiaAgenda(fecha);
        sistema.agregarDia(nuevoDia);

        System.out.println(
                "\nDia agregado correctamente."
        );
    }

    public void listarActividadesDeUnDia() throws IOException {

        System.out.println();
        System.out.println("----- ACTIVIDADES DE UN DIA -----");

        LocalDate fecha = leerFecha(
                "Ingrese fecha (dd/MM/yyyy): "
        );

        DiaAgenda dia = sistema.buscarDia(fecha);

        if (dia == null) {

            System.out.println(
                    "\nNo existe un dia registrado con esa fecha."
            );

            return;
        }

        System.out.println(
                "\nActividades del "
                        + fecha.format(formatoFecha)
                        + ":"
        );

        dia.mostrarActividades();
    }


    public void buscarDia() throws IOException {

        System.out.println();
        System.out.println("----- BUSCAR DIA -----");

        LocalDate fecha = leerFecha("Ingrese fecha (dd/MM/yyyy): ");
        DiaAgenda dia = sistema.buscarDia(fecha);

        if (dia != null) {
            System.out.println("Fecha: " + fecha.format(formatoFecha) + ":");
            dia.mostrarActividades();

        } else {
            System.out.println("No se encontro el dia solicitado.");
        }
    }

    public void editarDia() throws IOException {

        System.out.println();
        System.out.println("----- EDITAR DIA -----");

        LocalDate fechaActual = leerFecha(
                "Ingrese fecha del dia a editar (dd/MM/yyyy): "
        );

        LocalDate nuevaFecha = leerFecha(
                "Ingrese nueva fecha (dd/MM/yyyy): "
        );

        boolean editado = sistema.editarFechaDia(fechaActual, nuevaFecha);

        if (editado) {
            System.out.println("Fecha del dia editada correctamente.");

        } else {
            System.out.println("No se pudo editar la fecha del dia.");
        }
    }

    public void eliminarDia() throws IOException {

        System.out.println();
        System.out.println("----- ELIMINAR DIA -----");

        LocalDate fecha = leerFecha(
                "Ingrese fecha del dia a eliminar (dd/MM/yyyy): "
        );

        boolean eliminado = sistema.eliminarDia(fecha);

        if (eliminado) {
            System.out.println("Dia eliminado correctamente.");

        } else {
            System.out.println("No se encontro el dia solicitado.");
        }
    }

    public void editarActividad() throws IOException {

        System.out.println();
        System.out.println("----- EDITAR ACTIVIDAD -----");

        int id = leerEntero("Ingrese ID de la actividad a editar: ");

        Actividad actividad;

        try {

            actividad = sistema.buscarActividad(id);

        } catch (ActividadNoEncontradaException error) {

            System.out.println("\n" + error.getMessage());

            return;
        }

        System.out.println("Actividad encontrada:");
        System.out.println(actividad.mostrarActividad());

        String nuevoTitulo = leerTexto("Ingrese nuevo titulo: ");

        LocalTime nuevaHoraInicio = leerHora(
                "Ingrese nueva hora de inicio (HH:mm): "
        );

        LocalTime nuevaHoraFin = leerHoraFin(nuevaHoraInicio);

        String nuevaDescripcion = leerTexto(
                "Ingrese nueva descripcion: "
        );

        String nuevoDatoEspecifico = leerTexto(
                "Ingrese nueva " + actividad.getNombreDatoEspecifico() + ": "
        );

        try {

            actividad.actualizarHorario(
                    nuevaHoraInicio,
                    nuevaHoraFin
            );

            actividad.setTitulo(nuevoTitulo);
            actividad.setDescripcion(nuevaDescripcion);
            actividad.setDatoEspecifico(nuevoDatoEspecifico);

            System.out.println(
                    "Actividad editada correctamente."
            );

        } catch (HorarioInvalidoException error) {

            System.out.println(
                    "\nNo se pudo editar la actividad: "
                            + error.getMessage()
            );
        }
    }

    public void eliminarActividad() throws IOException {

        System.out.println();
        System.out.println("----- ELIMINAR ACTIVIDAD -----");

        int id = leerEntero("Ingrese ID de la actividad a eliminar: ");

        try {

            boolean eliminada = sistema.eliminarActividadPorId(id);

            if (eliminada) {
                System.out.println("Actividad eliminada correctamente.");
            }

        } catch (ActividadNoEncontradaException error) {

            System.out.println("\n" + error.getMessage());
        }
    }

    public void buscarHorarioDisponible() throws IOException {

        System.out.println();
        System.out.println("----- BUSCAR HORARIO DISPONIBLE -----");

        LocalDate fecha = leerFecha("Ingrese fecha (dd/MM/yyyy): ");

        int duracionMinutos = 0;

        while (duracionMinutos <= 0 || duracionMinutos > 1440) {

            duracionMinutos = leerEntero("Ingrese duracion (en minutos): ");

            if (duracionMinutos <= 0 || duracionMinutos > 1440) {
                System.out.println(
                        "La duracion debe ser mayor a 0 y no superar 1440 minutos."
                );
            }
        }

        String resultado = sistema.buscarHorarioDisponible(
                fecha, duracionMinutos
        );

        System.out.println(resultado);
    }

    // Solicita el tipo hasta que se ingrese una opcion valida.
    public String leerTipoActividad() throws IOException {

        while (true) {

            System.out.println("Tipo de actividad");
            System.out.println("1. Academica");
            System.out.println("2. Proyecto");
            System.out.println("3. Personal");
            System.out.println("4. Otro");

            System.out.print("Seleccione un tipo: ");
            String tipo = lector.readLine();

            if (tipo.equals("1") || tipo.equals("2")
                    || tipo.equals("3") || tipo.equals("4")) {

                return tipo;
            }

            System.out.println("\nTipo no valido. Intente nuevamente.\n");
        }
    }

    public Actividad crearActividadPorTipo(
            int id,
            String tipo,
            String titulo,
            LocalTime horaInicio,
            LocalTime horaFin,
            String descripcion) throws IOException,HorarioInvalidoException {

        if (tipo.equals("1")) {

            String asignatura = leerTexto("Ingrese asignatura: ");

            return new ActividadAcademica(
                    id, titulo, horaInicio, horaFin, descripcion, asignatura
            );

        } else if (tipo.equals("2")) {

            String nombreProyecto = leerTexto("Ingrese nombre del proyecto: ");

            return new ActividadProyecto(
                    id, titulo, horaInicio, horaFin, descripcion, nombreProyecto
            );

        } else if (tipo.equals("3")) {

            String lugar = leerTexto("Ingrese lugar: ");

            return new ActividadPersonal(
                    id, titulo, horaInicio, horaFin, descripcion, lugar
            );

        } else if (tipo.equals("4")) {

            String lugar = leerTexto("Ingrese lugar: ");

            return new ActividadOtro(
                    id, titulo, horaInicio, horaFin, descripcion, lugar
            );
        }

        System.out.println("Tipo no valido.");

        return null;
    }

    public String leerTexto(String mensaje) throws IOException {

        System.out.print(mensaje);
        String texto = lector.readLine();

        while (texto.trim().length() == 0) {

            System.out.println("\nEl texto no puede estar vacio.");

            System.out.print(mensaje);
            texto = lector.readLine();
        }

        return texto;
    }

    public LocalDate leerFecha(String mensaje) throws IOException {

        while (true) {

            System.out.print(mensaje);
            String texto = lector.readLine();

            try {
                return LocalDate.parse(texto, formatoFecha);

            } catch (DateTimeParseException error) {
                System.out.println(
                        "\nFecha invalida. Ejemplo valido: 23/08/2026"
                );
            }
        }
    }

    public LocalTime leerHora(String mensaje) throws IOException {

        while (true) {

            System.out.print(mensaje);
            String texto = lector.readLine();

            try {
                return LocalTime.parse(texto, formatoHora);

            } catch (DateTimeParseException error) {
                System.out.println(
                        "\nHora invalida. Ejemplo valido: 14:30"
                );
            }
        }
    }

    // La hora de fin debe ser posterior a la hora de inicio.
    public LocalTime leerHoraFin(LocalTime horaInicio) throws IOException {

        while (true) {

            LocalTime horaFin = leerHora("Ingrese hora de fin (HH:mm): ");

            if (horaFin.isAfter(horaInicio)) {
                return horaFin;
            }

            System.out.println(
                    "La hora de fin debe ser posterior a la hora de inicio."
            );
        }
    }

    public int leerMes() throws IOException {

        while (true) {

            int mes = leerEntero("Ingrese mes (1-12): ");

            if (mes >= 1 && mes <= 12) {
                return mes;
            }

            System.out.println(
                    "Mes invalido. Ingrese un valor entre 1 y 12."
            );
        }
    }

    public int leerAnio() throws IOException {

        while (true) {

            int anio = leerEntero("Ingrese anio: ");

            if (anio > 0) {
                return anio;
            }

            System.out.println("El anio debe ser mayor a 0.");
        }
    }

    public int leerEntero(String mensaje) throws IOException {

        while (true) {

            String texto = leerTexto(mensaje);

            try {
                return Integer.parseInt(texto);

            } catch (NumberFormatException error) {
                System.out.println(
                        "Entrada invalida. Ingrese un numero entero."
                );
            }
        }
    }
}
