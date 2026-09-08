import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

public class SistemaAgenda {

    private TreeMap<LocalDate, DiaAgenda> dias;
    private DateTimeFormatter formatoFecha;
    private int siguienteId;

    public SistemaAgenda() {
        this.dias = new TreeMap<LocalDate, DiaAgenda>();
        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.siguienteId = 1;
    }

    public TreeMap<LocalDate, DiaAgenda> getDias() {
        return dias;
    }

    public void setDias(TreeMap<LocalDate, DiaAgenda> dias) {
        this.dias = dias;
    }

    public DateTimeFormatter getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha(DateTimeFormatter formatoFecha) {
        this.formatoFecha = formatoFecha;
    }

    public int getSiguienteId() {
        return siguienteId;
    }

    public void setSiguienteId(int siguienteId) {
        this.siguienteId = siguienteId;
    }

    // Genera un identificador unico para cada actividad.
    public int generarIdActividad() {

        int idGenerado = siguienteId;
        siguienteId++;

        return idGenerado;
    }

    public void agregarDia(DiaAgenda dia) {
        dias.putIfAbsent(dia.getFecha(), dia);
    }

    // Lista solamente los dias registrados,
    // independiente de sus actividades.
    public void listarDias() {

        if (dias.isEmpty()) {
            System.out.println("No hay dias registrados.");
            return;
        }

        System.out.println("\n----- DIAS REGISTRADOS -----");

        for (LocalDate fecha : dias.keySet()) {
            System.out.println(fecha.format(formatoFecha));
        }
    }



    // Agrega una actividad a una fecha.
    // Si el dia no existe, se crea automaticamente.
    public void agregarActividad(LocalDate fecha, Actividad actividad) {

        DiaAgenda dia = dias.get(fecha);

        if (dia == null) {
            dia = new DiaAgenda(fecha);
            agregarDia(dia);
        }

        dia.agregarActividad(actividad);
    }

    public Actividad buscarActividad(int id) {

        for (DiaAgenda dia : dias.values()) {

            Actividad actividad = dia.buscarActividadPorId(id);

            if (actividad != null) {
                return actividad;
            }
        }

        return null;
    }

    public Actividad buscarActividad(String titulo) {

        for (DiaAgenda dia : dias.values()) {

            Actividad actividad = dia.buscarActividadPorTitulo(titulo);

            if (actividad != null) {
                return actividad;
            }
        }

        return null;
    }

    // Muestra un dia con el mismo formato en todas las consultas.
    private void mostrarDiaAgenda(DiaAgenda dia) {

        System.out.println("----------------------------");
        System.out.println("Fecha: " + dia.getFecha().format(formatoFecha));

        dia.mostrarActividades();
    }

    // Muestra toda la agenda.
    public void mostrarDias() {

        if (dias.isEmpty()) {
            System.out.println("La agenda no tiene dias registrados.");
            return;
        }

        for (DiaAgenda dia : dias.values()) {
            mostrarDiaAgenda(dia);
        }
    }

    public void mostrarDia(LocalDate fecha) {

        DiaAgenda dia = dias.get(fecha);

        if (dia == null) {
            System.out.println("No hay actividades registradas para esa fecha.");
            return;
        }

        mostrarDiaAgenda(dia);
    }

    public void mostrarMes(int mes, int anio) {

        boolean encontrado = false;

        for (DiaAgenda dia : dias.values()) {

            LocalDate fecha = dia.getFecha();

            if (fecha.getMonthValue() == mes && fecha.getYear() == anio) {
                mostrarDiaAgenda(dia);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No hay actividades registradas para ese mes.");
        }
    }

    public void mostrarAnio(int anio) {

        boolean encontrado = false;

        for (DiaAgenda dia : dias.values()) {

            if (dia.getFecha().getYear() == anio) {
                mostrarDiaAgenda(dia);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No hay actividades registradas para ese anio.");
        }
    }

    // Muestra actividades correspondientes a fechas anteriores a hoy.
    public void mostrarHistorial() {

        LocalDate hoy = LocalDate.now();
        boolean encontrado = false;

        for (DiaAgenda dia : dias.values()) {

            if (dia.getFecha().isBefore(hoy)) {
                mostrarDiaAgenda(dia);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No hay actividades en el historial.");
        }
    }

    public String buscarHorarioDisponible(LocalDate fecha, int duracionMinutos) {

        if (duracionMinutos <= 0 || duracionMinutos > 24 * 60) {
            return "Duracion invalida.";
        }

        DiaAgenda dia = dias.get(fecha);

        if (dia == null) {
            return "El dia completo esta disponible (00:00 a 23:59).";
        }

        return dia.buscarHorarioDisponible(duracionMinutos);
    }

    public DiaAgenda buscarDia(LocalDate fecha) {
        return dias.get(fecha);
    }

    public boolean editarFechaDia(LocalDate fechaActual, LocalDate nuevaFecha) {

        DiaAgenda dia = dias.get(fechaActual);

        if (dia == null) {
            return false;
        }

        if (!fechaActual.equals(nuevaFecha) && dias.containsKey(nuevaFecha)) {
            return false;
        }

        dias.remove(fechaActual);

        dia.setFecha(nuevaFecha);
        dias.put(nuevaFecha, dia);

        return true;
    }

    public boolean eliminarDia(LocalDate fecha) {

        if (!dias.containsKey(fecha)) {
            return false;
        }

        dias.remove(fecha);

        return true;
    }

    public boolean eliminarActividadPorId(int id) {

        for (DiaAgenda dia : dias.values()) {

            if (dia.eliminarActividadPorId(id)) {
                return true;
            }
        }

        return false;
    }

    // Datos utilizados para probar las funcionalidades del sistema.
    // Datos utilizados para probar las funcionalidades del sistema.
    public void cargarDatosIniciales() {

        try {

            LocalDate ayer = LocalDate.now().minusDays(1);
            LocalDate manana = LocalDate.now().plusDays(1);
            LocalDate pasadoManana = LocalDate.now().plusDays(2);

            ActividadAcademica academica = new ActividadAcademica(
                    generarIdActividad(),
                    "Clase de Programacion Avanzada",
                    LocalTime.of(10, 0),
                    LocalTime.of(11, 30),
                    "Clase INF2236",
                    "Programacion Avanzada"
            );

            ActividadProyecto proyecto = new ActividadProyecto(
                    generarIdActividad(),
                    "Reunion proyecto SIA",
                    LocalTime.of(15, 0),
                    LocalTime.of(16, 0),
                    "Revision del avance del proyecto",
                    "SIA-GestionAgenda"
            );

            ActividadPersonal personal = new ActividadPersonal(
                    generarIdActividad(),
                    "Control medico",
                    LocalTime.of(9, 0),
                    LocalTime.of(10, 0),
                    "Control general",
                    "Centro medico"
            );

            ActividadOtro otro = new ActividadOtro(
                    generarIdActividad(),
                    "Tramite personal",
                    LocalTime.of(12, 0),
                    LocalTime.of(13, 0),
                    "Realizar tramite pendiente",
                    "Valparaiso"
            );

            ActividadPersonal historica = new ActividadPersonal(
                    generarIdActividad(),
                    "Entrenamiento",
                    LocalTime.of(18, 0),
                    LocalTime.of(19, 0),
                    "Actividad utilizada para probar el historial",
                    "Gimnasio"
            );

            agregarActividad(manana, academica);
            agregarActividad(manana, proyecto);
            agregarActividad(pasadoManana, personal);
            agregarActividad(pasadoManana, otro);
            agregarActividad(ayer, historica);

        } catch (HorarioInvalidoException error) {

            System.out.println(
                    "Error al cargar datos iniciales: "
                            + error.getMessage()
            );
        }
    }
}
