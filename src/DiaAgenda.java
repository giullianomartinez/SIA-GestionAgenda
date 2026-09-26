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

    public ArrayList<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(ArrayList<Actividad> actividades) {
        this.actividades = actividades;
    }

    public void agregarActividad(Actividad actividad) {
        actividades.add(actividad);
    }

    public int cantidadActividades() {
        return actividades.size();
    }

    public Actividad obtenerActividad(int posicion) {
        return actividades.get(posicion);
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
        int minutoActual = 0;
        boolean encontrado = false;
    
        while (minutoActual + duracionMinutos <= 24 * 60) {
    
            int minutoFin = minutoActual + duracionMinutos;
            boolean disponible = true;
    
            for (int i = 0; i < actividades.size(); i++) {
    
                Actividad actividad = actividades.get(i);
    
                int inicioActividad = actividad.getHoraInicio().getHour() * 60
                        + actividad.getHoraInicio().getMinute();
    
                int finActividad = actividad.getHoraFin().getHour() * 60
                        + actividad.getHoraFin().getMinute();
    
                if (inicioActividad < minutoFin
                        && finActividad > minutoActual) {
    
                    disponible = false;
                    minutoActual = finActividad;
                    break;
                }
            }
    
            if (disponible) {
    
                horarios.append("- ")
                        .append(formatearMinutos(minutoActual))
                        .append(" a ")
                        .append(formatearMinutos(minutoFin))
                        .append("\n");
    
                encontrado = true;
                minutoActual = minutoFin;
            }
        }
    
        if (!encontrado) {
            return "No hay horarios disponibles para la duracion solicitada.";
        }
    
        return "Horarios disponibles:\n"
                + horarios.toString().trim();
    }

    public String formatearMinutos(int minutos) {
    
        if (minutos == 24 * 60) {
            return "24:00";
        }
    
        int hora = minutos / 60;
        int minuto = minutos % 60;
    
        return String.format("%02d:%02d", hora, minuto);
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
