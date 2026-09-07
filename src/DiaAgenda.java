import java.time.LocalDate;
import java.time.LocalTime;
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

    public void agregarActividad(Actividad actividad) {

        actividades.add(actividad);
    }

    // Muestra todas las actividades registradas en este dia.
    public void mostrarActividades() {

        if (actividades.isEmpty()) {
            System.out.println("\nNo hay actividades registradas.");
            return;
        }

        for (Actividad actividad : actividades) {
            System.out.println(actividad.mostrarActividad());
        }
    }

    // Muestra unicamente las actividades que pertenecen al tipo indicado.
    public void mostrarActividades(String tipoActividad) {

        boolean encontrada = false;

        for (Actividad actividad : actividades) {

            if (actividad.getTipoActividad().equalsIgnoreCase(tipoActividad)) {

                System.out.println(actividad.mostrarActividad());
                encontrada = true;
            }
        }

        if (!encontrada) {
            System.out.println("\nNo hay actividades de este tipo.");
        }
    }

    // Busca bloques disponibles del tamaño solicitado dentro del dia.
    public String buscarHorarioDisponible(int duracionMinutos) {

        if (duracionMinutos <= 0 || duracionMinutos > 24 * 60) {
            return "Duracion invalida.";
        }

        StringBuilder horarios = new StringBuilder();

        LocalTime hora = LocalTime.of(0, 0);

        boolean encontrado = false;

        while (hora.getHour() * 60
                + hora.getMinute()
                + duracionMinutos <= 24 * 60) {

            LocalTime horaFin = hora.plusMinutes(duracionMinutos);

            boolean disponible = true;

            for (int i = 0; i < actividades.size(); i++) {

                Actividad actividad = actividades.get(i);

                if (actividad.getHoraInicio().isBefore(horaFin)
                        && actividad.getHoraFin().isAfter(hora)) {

                    disponible = false;

                    hora = actividad.getHoraFin();

                    break;
                }
            }

            if (disponible) {

                horarios.append("- ")
                        .append(hora)
                        .append(" a ")
                        .append(horaFin)
                        .append("\n");

                encontrado = true;

                hora = horaFin;
            }

            // LocalTime vuelve a 00:00 al superar las 23:59.
            if (hora.equals(LocalTime.MIDNIGHT) && encontrado) {
                break;
            }
        }

        if (!encontrado) {
            return "No hay horarios disponibles para la duracion solicitada.";
        }

        return "Horarios disponibles:\n"
                + horarios.toString().trim();
    }

    // Busca una actividad por ID dentro de este dia.
    public Actividad buscarActividadPorId(int id) {

        for (int i = 0; i < actividades.size(); i++) {

            Actividad actividad = actividades.get(i);

            if (actividad.getId() == id) {
                return actividad;
            }
        }

        return null;
    }

    // Busca una actividad por titulo dentro de este dia.
    public Actividad buscarActividadPorTitulo(String titulo) {

        for (int i = 0; i < actividades.size(); i++) {

            Actividad actividad = actividades.get(i);

            if (actividad.getTitulo().equalsIgnoreCase(titulo)) {
                return actividad;
            }
        }

        return null;
    }

    // Elimina una actividad por ID dentro de este dia.
    public boolean eliminarActividadPorId(int id) {

        for (int i = 0; i < actividades.size(); i++) {

            Actividad actividad = actividades.get(i);

            if (actividad.getId() == id) {

                actividades.remove(i);

                return true;
            }
        }

        return false;
    }
}
