import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

public class SistemaAgenda {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private TreeMap<LocalDate, DiaAgenda> dias;

    public SistemaAgenda() {
        this.dias = new TreeMap<LocalDate, DiaAgenda>();
    }

    public TreeMap<LocalDate, DiaAgenda> getDias() {
        return dias;
    }

    public void setDias(TreeMap<LocalDate, DiaAgenda> dias) {
        this.dias = dias;
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
            System.out.println("Fecha: " + dia.getFecha().format(FORMATO_FECHA));
            dia.mostrarActividades();
        }
    }
}
