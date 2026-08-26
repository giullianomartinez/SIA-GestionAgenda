import java.time.LocalTime;

public class ActividadProyecto extends Actividad {

    private String nombreProyecto;

    public ActividadProyecto(int id, String titulo, LocalTime horaInicio,
                             LocalTime horaFin, String descripcion,
                             String nombreProyecto) {

        super(id, titulo, horaInicio, horaFin, descripcion, "Proyecto");
        this.nombreProyecto = nombreProyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
}
