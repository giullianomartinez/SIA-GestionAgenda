import java.time.LocalTime;

public class ActividadPersonal extends Actividad {

    private String lugar;

    public ActividadPersonal(int id, String titulo, LocalTime horaInicio,
                             LocalTime horaFin, String descripcion,
                             String lugar) {

        super(id, titulo, horaInicio, horaFin, descripcion, "Personal");
        this.lugar = lugar;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

}
