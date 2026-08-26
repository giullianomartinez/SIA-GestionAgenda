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
}
