import java.time.LocalTime;

public class ActividadAcademica extends Actividad {

    private String asignatura;

    public ActividadAcademica(int id, String titulo, LocalTime horaInicio,
                              LocalTime horaFin, String descripcion,
                              String asignatura) {

        super(id, titulo, horaInicio, horaFin, descripcion, "Academica");
        this.asignatura = asignatura;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

}
