import java.time.LocalTime;

public class ActividadPersonal extends Actividad {

    private String lugar;

    public ActividadPersonal(int id, String titulo, LocalTime horaInicio,
                             LocalTime horaFin, String descripcion, String lugar) throws  HorarioInvalidoException{

        super(id, titulo, horaInicio, horaFin, descripcion, "Personal");
        this.lugar = lugar;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    @Override
    public String getNombreDatoEspecifico() {
        return "lugar";
    }

    @Override
    public String getDatoEspecifico() {
        return lugar;
    }

    @Override
    public void setDatoEspecifico(String dato) {
        setLugar(dato);
    }

    @Override
    public String mostrarActividad() {
        return super.mostrarActividad() + " | Lugar: " + lugar;
    }
}
