import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;
import java.time.LocalTime;

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

    // Genera un identificador unico y aumenta el contador para la siguiente actividad.
    public int generarIdActividad() {

        int idGenerado = siguienteId;

        siguienteId = siguienteId + 1;

        return idGenerado;
    }

    public void agregarDia(DiaAgenda dia) {

        dias.put(dia.getFecha(), dia);
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

    // Busca una actividad por su identificador unico.
    public Actividad buscarActividad(int id) {

        for (DiaAgenda dia : dias.values()) {

            Actividad actividad = dia.buscarActividadPorId(id);

            if (actividad != null) {
                return actividad;
            }
        }

        return null;
    }

    // Busca la primera actividad cuyo titulo coincida.
    public Actividad buscarActividad(String titulo) {

        for (DiaAgenda dia : dias.values()) {

            Actividad actividad = dia.buscarActividadPorTitulo(titulo);

            if (actividad != null) {
                return actividad;
            }
        }

        return null;
    }


    // Recorre los dias en orden cronologico y muestra sus actividades.
    public void mostrarDias() {

        if (dias.isEmpty()) {

            System.out.println("La agenda no tiene dias registrados.");

            return;
        }

        for (DiaAgenda dia : dias.values()) {

            System.out.println("----------------------------");

            System.out.println(
                    "Fecha: "
                    + dia.getFecha().format(formatoFecha)
            );

            dia.mostrarActividades();
        }
    }

    public String buscarHorarioDisponible(LocalDate fecha,
                                           int duracionMinutos) {

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

    public boolean editarFechaDia(LocalDate fechaActual,
                                  LocalDate nuevaFecha) {

        DiaAgenda dia = dias.get(fechaActual);

        if (dia == null) {
            return false;
        }

        if (!fechaActual.equals(nuevaFecha)
                && dias.containsKey(nuevaFecha)) {

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

    // Carga datos utilizados exclusivamente para probar
    // las funcionalidades del sistema.
    public void cargarDatosIniciales() {

        LocalDate fecha1 = LocalDate.of(2026, 9, 8);
        LocalDate fecha2 = LocalDate.of(2026, 9, 9);

        ActividadAcademica academica
                = new ActividadAcademica(
                        generarIdActividad(),
                        "Clase de Programacion Avanzada",
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 30),
                        "Clase INF2236",
                        "Programacion Avanzada"
                );

        ActividadProyecto proyecto
                = new ActividadProyecto(
                        generarIdActividad(),
                        "Reunion proyecto SIA",
                        LocalTime.of(15, 0),
                        LocalTime.of(16, 0),
                        "Revision del avance del proyecto",
                        "SIA-GestionAgenda"
                );

        ActividadPersonal personal
                = new ActividadPersonal(
                        generarIdActividad(),
                        "Control medico",
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0),
                        "Control general",
                        "Centro medico"
                );

        ActividadOtro otro
                = new ActividadOtro(
                        generarIdActividad(),
                        "Tramite personal",
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        "Realizar tramite pendiente",
                        "Valparaiso"
                );

        agregarActividad(fecha1, academica);
        agregarActividad(fecha1, proyecto);

        agregarActividad(fecha2, personal);
        agregarActividad(fecha2, otro);
    }
}
