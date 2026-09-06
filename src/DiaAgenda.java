import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalTime;

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

    public void agregarActividad(Actividad actividad) {
        actividades.add(actividad);
    }

    public void mostrarActividades() {
        if (actividades.isEmpty()) {
            System.out.println("No hay actividades registradas.");
            return;
        }

        for (Actividad actividad : actividades) {
            System.out.println(actividad.mostrarActividad());
        }
    }

    public String buscarHorarioDisponible(int duracionMinutos) {
        if(duracionMinutos <= 0 || duracionMinutos > 24*60) {
            return "Duración inválida.";
        }

        LocalTime hora = LocalTime.of(0, 0);

        while(hora.getHour() * 60 + hora.getMinute() + duracionMinutos <= 24 * 60) {
            LocalTime horaFin = hora.plusMinutes(duracionMinutos);
            boolean disponible = true;

            for (int i = 0; i < actividades.size(); i++) {
                Actividad actividad = actividades.get(i);
                if (actividad.getHoraInicio().isBefore(horaFin) && actividad.getHoraFin().isAfter(hora)) {
                    disponible = false;
                    break;
                }
            }

            if (disponible) {
                return "Horario disponible: " + hora + " - " + horaFin;
            }

            hora = hora.plusMinutes(1);
        }

        return "Horario disponible.";
    }

    public Actividad buscarActividadPorId(int id) {
        for (int i = 0; i < actividades.size(); i++) {
            Actividad act = actividades.get(i);
            if (act.getId() == id) {
                return act;
            }
        }
        return null;
    }

    public boolean eliminarActividadPorId(int id) {
        for (int i = 0; i < actividades.size(); i++) {
            Actividad act = actividades.get(i);
            if (act.getId() == id) {
                actividades.remove(i);
                return true;
            }
        }
        return false;
    }

    
}
