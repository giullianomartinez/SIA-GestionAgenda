import java.time.LocalTime;
import java.time.LocalDate;
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

    public int generarIdActividad() {
        int idGenerado = siguienteId;
        siguienteId = siguienteId + 1;
        return idGenerado;
    }

    public void agregarDia(DiaAgenda dia) {
        dias.put(dia.getFecha(), dia);
    }

    public void agregarActividad(LocalDate fecha, Actividad actividad) {
        DiaAgenda dia = dias.get(fecha);

        if (dia == null) {
            dia = new DiaAgenda(fecha);
            agregarDia(dia);
        }

        dia.agregarActividad(actividad);
    }

    public void mostrarDias() {
        if (dias.isEmpty()) {
            System.out.println("La agenda no tiene dias registrados.");
            return;
        }

        for (DiaAgenda dia : dias.values()) {
            System.out.println("----------------------------");
            System.out.println("Fecha: " + dia.getFecha().format(formatoFecha));
            dia.mostrarActividades();
        }
    }

    // Carga actividades utilizadas exclusivamente para ejecutar el sistema en modo prueba.
    public void cargarDatosIniciales() {

        LocalDate fecha1 = LocalDate.of(2026, 9, 8);
        LocalDate fecha2 = LocalDate.of(2026, 9, 9);

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

        agregarActividad(fecha1, academica);
        agregarActividad(fecha1, proyecto);

        agregarActividad(fecha2, personal);
        agregarActividad(fecha2, otro);
    }
}
