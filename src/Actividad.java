import java.time.LocalTime;

public class Actividad {

    private int id;
    private String titulo;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String descripcion;
    private String tipoActividad;

    public Actividad(int id, String titulo, LocalTime horaInicio,
                     LocalTime horaFin, String descripcion, String tipoActividad) throws HorarioInvalidoException {

        if (!esHorarioValido(horaInicio, horaFin)) {
            throw new HorarioInvalidoException(
                    "La hora de fin debe ser posterior a la hora de inicio."
            );
        }

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

    public void setHoraInicio(LocalTime horaInicio) throws HorarioInvalidoException {

        if (!esHorarioValido(horaInicio, this.horaFin)) {
            throw new HorarioInvalidoException(
                    "La hora de inicio debe ser anterior a la hora de fin."
            );
        }

        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) throws HorarioInvalidoException {

        if (!esHorarioValido(this.horaInicio, horaFin)) {
            throw new HorarioInvalidoException(
                    "La hora de fin debe ser posterior a la hora de inicio."
            );
        }

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

    private boolean esHorarioValido(LocalTime horaInicio, LocalTime horaFin) {

        if (horaInicio == null || horaFin == null) {
            return false;
        }

        return horaFin.isAfter(horaInicio);
    }

    public void actualizarHorario(LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) throws HorarioInvalidoException {

        if (!esHorarioValido(nuevaHoraInicio, nuevaHoraFin)) {
            throw new HorarioInvalidoException("La hora de fin debe ser posterior a la hora de inicio.");
        }

        this.horaInicio = nuevaHoraInicio;
        this.horaFin = nuevaHoraFin;

    }

    // Las subclases sobrescriben estos metodos
    // para trabajar con su atributo propio.
    public String getNombreDatoEspecifico() {
        return "dato especifico";
    }

    public String getDatoEspecifico() {
        return "";
    }

    public void setDatoEspecifico(String dato) {
    }

    public String mostrarActividad() {

        String texto = horaInicio + " - " + horaFin
                + " | " + titulo
                + " | " + getTipoActividad()
                + " | " + descripcion;

        return texto;
    }

    // Sobrecarga de metodos para posponer una actividad.
    public void posponer(int minutos) throws  HorarioInvalidoException {

        LocalTime nuevaHoraInicio = horaInicio.plusMinutes(minutos);
        LocalTime nuevaHoraFin = horaFin.plusMinutes(minutos);

        actualizarHorario(nuevaHoraInicio, nuevaHoraFin);
    }

    public void posponer(LocalTime horaInicio, LocalTime horaFin) throws HorarioInvalidoException {
        actualizarHorario(horaInicio, horaFin);
    }
}
