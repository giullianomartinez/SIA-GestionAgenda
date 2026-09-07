import java.time.LocalTime;

public class Actividad {

    private int id;
    private String titulo;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String descripcion;
    private String tipoActividad;

    public Actividad(
            int id,
            String titulo,
            LocalTime horaInicio,
            LocalTime horaFin,
            String descripcion,
            String tipoActividad) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoActividad = tipoActividad;

        if (!actualizarHorario(
                horaInicio,
                horaFin)) {

            throw new IllegalArgumentException(
                    "La hora de fin debe ser "
                    + "posterior a la hora de inicio."
            );
        }
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

    public void setTitulo(
            String titulo) {

        this.titulo = titulo;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(
            LocalTime horaInicio) {

        if (horaInicio == null
                || (horaFin != null
                && !horaFin.isAfter(
                        horaInicio))) {

            throw new IllegalArgumentException(
                    "La hora de inicio debe ser "
                    + "anterior a la hora de fin."
            );
        }

        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(
            LocalTime horaFin) {

        if (horaFin == null
                || (horaInicio != null
                && !horaFin.isAfter(
                        horaInicio))) {

            throw new IllegalArgumentException(
                    "La hora de fin debe ser "
                    + "posterior a la hora de inicio."
            );
        }

        this.horaFin = horaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(
            String descripcion) {

        this.descripcion = descripcion;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(
            String tipoActividad) {

        this.tipoActividad = tipoActividad;
    }

    // Cambia las dos horas al mismo tiempo
    // y evita almacenar un horario invalido.
    public boolean actualizarHorario(
            LocalTime nuevaHoraInicio,
            LocalTime nuevaHoraFin) {

        if (nuevaHoraInicio == null
                || nuevaHoraFin == null
                || !nuevaHoraFin.isAfter(
                        nuevaHoraInicio)) {

            return false;
        }

        this.horaInicio = nuevaHoraInicio;
        this.horaFin = nuevaHoraFin;

        return true;
    }

    // Indica el nombre del atributo propio
    // que posee cada subclase.
    public String getNombreDatoEspecifico() {

        return "dato especifico";
    }

    // Permite modificar el atributo propio
    // utilizando polimorfismo.
    public void setDatoEspecifico(
            String dato) {

        // Las subclases sobrescriben
        // este metodo.
    }

    // Devuelve la informacion principal
    // de la actividad.
    public String mostrarActividad() {

        String texto
                = horaInicio
                + " - "
                + horaFin
                + " | "
                + titulo
                + " | "
                + getTipoActividad()
                + " | "
                + descripcion;

        return texto;
    }

    // Posponen la actividad utilizando
    // sobrecarga de metodos.
    public void posponer(
            int minutos) {

        if (horaInicio != null
                && horaFin != null) {

            LocalTime nuevaHoraInicio
                    = horaInicio.plusMinutes(
                            minutos
                    );

            LocalTime nuevaHoraFin
                    = horaFin.plusMinutes(
                            minutos
                    );

            actualizarHorario(
                    nuevaHoraInicio,
                    nuevaHoraFin
            );
        }
    }

    public void posponer(
            LocalTime horaInicio,
            LocalTime horaFin) {

        actualizarHorario(
                horaInicio,
                horaFin
        );
    }
}
