import java.time.LocalTime;

public class Actividad {

    private int id;
    private String titulo;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String descripcion;
    private String tipoActividad;

    public Actividad(int id, String titulo, LocalTime horaInicio,
                     LocalTime horaFin, String descripcion,
                     String tipoActividad) {

        this.id = id;
        this.titulo = titulo;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.descripcion = descripcion;
        this.tipoActividad = tipoActividad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    
    public String mostrarActividad() {
        String texto = horaInicio + " - " + horaFin
                + " | " + titulo
                + " | " + getTipoActividad()
                + " | " + descripcion;

        return texto;
    }
}
