import java.time.LocalDate;
import java.util.ArrayList;

public class DiaAgenda {

    private LocalDate fecha;
    private ArrayList<Actividad> actividades;

    public DiaAgenda(LocalDate fecha) {
        this.fecha = fecha;
        this.actividades = new ArrayList<Actividad>();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(ArrayList<Actividad> actividades) {
        this.actividades = actividades;
    }

    // Agrega una actividad a la lista de actividades correspondientes a este día.
    public void agregarActividad(Actividad actividad) {
        actividades.add(actividad);
    }

    // Muestra todas las actividades registradas en este día.
    public void mostrarActividades() {
        if (actividades.isEmpty()) {
            System.out.println("\nNo hay actividades registradas.");
            return;
        }

        for (Actividad actividad : actividades) {
            System.out.println(actividad.mostrarActividad());
        }
    }
}
