import java.time.LocalTime;

public class ActividadOtro extends Actividad {

    private String lugar;

    public ActividadOtro(int id, String titulo, LocalTime horaInicio,
                             LocalTime horaFin, String descripcion,
                             String lugar) {

        super(id, titulo, horaInicio, horaFin, descripcion, "Otro");
        this.lugar = lugar;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

}

