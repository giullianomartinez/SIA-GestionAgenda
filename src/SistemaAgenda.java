import java.time.LocalDate;
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

    public String buscarHorarioDisponible(LocalDate fecha, int duracionMinutos) {
        if(duracionMinutos <= 0 || duracionMinutos > 24*60) {
            return "Duración inválida.";
        }

        DiaAgenda dia = dias.get(fecha);
        if (dia == null) {
            LocalTime Inicio = LocalTime.of(0, 0);
            LocalTime Fin = Inicio.plusMinutes(duracionMinutos);
            return "Horario disponible: " + Inicio + " - " + Fin;
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
        if(!fechaActual.equals(nuevaFecha) && dias.containsKey(nuevaFecha)) {
            return false;
        }

        dias.remove(fechaActual);
        dia.setFecha(nuevaFecha);
        dias.put(nuevaFecha, dia);
        return true;
    }

    public boolean eliminarDia(LocalDate fecha) {
        DiaAgenda dia = dias.get(fecha);
        if (dia == null) {
            return false;
        }

        dias.remove(fecha);
        return true;
    }

    public Actividad buscarActividadPorId(int id) {
        for (DiaAgenda dia : dias.values()) {
            Actividad act = dia.buscarActividadPorId(id);
            if (act != null) {
                return act;
            }
        }
        return null;
    }

    public boolean eliminarActividadPorId(int id) {
        for (DiaAgenda dia : dias.values()) {
            if (dia.eliminarActividadPorId(id)) {
                return true;
            }
        }
        return false;
    }
    
}
