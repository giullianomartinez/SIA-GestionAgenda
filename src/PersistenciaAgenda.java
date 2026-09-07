import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PersistenciaAgenda {

    private String rutaCarpeta;
    private String rutaDias;
    private String rutaActividades;
    private DateTimeFormatter formatoFecha;
    private DateTimeFormatter formatoHora;

    public PersistenciaAgenda() {
        this.rutaCarpeta = "data";
        this.rutaDias = "data/dias.csv";
        this.rutaActividades = "data/actividades.csv";
        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm");
    }

    public void cargarDatos(SistemaAgenda sistema) {

        try {
            crearArchivos();
            cargarDias(sistema);
            cargarActividades(sistema);

        } catch (IOException error) {
            System.out.println("No se pudieron cargar los datos de la agenda.");
        }
    }

    public void guardarDatos(SistemaAgenda sistema) {

        try {
            crearArchivos();
            guardarDias(sistema);
            guardarActividades(sistema);

        } catch (IOException error) {
            System.out.println("No se pudieron guardar los datos de la agenda.");
        }
    }

    private void crearArchivos() throws IOException {

        File carpeta = new File(rutaCarpeta);

        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        File archivoDias = new File(rutaDias);

        if (!archivoDias.exists()) {

            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoDias));
            escritor.write("fecha");
            escritor.newLine();
            escritor.close();
        }

        File archivoActividades = new File(rutaActividades);

        if (!archivoActividades.exists()) {

            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoActividades));
            escritor.write("fecha;id;tipoActividad;titulo;horaInicio;horaFin;descripcion;datoEspecifico");
            escritor.newLine();
            escritor.close();
        }
    }

    private void guardarDias(SistemaAgenda sistema) throws IOException {

        BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaDias));

        escritor.write("fecha");
        escritor.newLine();

        for (DiaAgenda dia : sistema.getDias().values()) {

            escritor.write(dia.getFecha().format(formatoFecha));
            escritor.newLine();
        }

        escritor.close();
    }

    private void guardarActividades(SistemaAgenda sistema) throws IOException {

        BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaActividades));

        escritor.write("fecha;id;tipoActividad;titulo;horaInicio;horaFin;descripcion;datoEspecifico");
        escritor.newLine();

        for (DiaAgenda dia : sistema.getDias().values()) {

            for (int i = 0; i < dia.cantidadActividades(); i++) {

                Actividad actividad = dia.obtenerActividad(i);

                String linea = dia.getFecha().format(formatoFecha)
                        + ";" + actividad.getId()
                        + ";" + limpiarTexto(actividad.getTipoActividad())
                        + ";" + limpiarTexto(actividad.getTitulo())
                        + ";" + actividad.getHoraInicio().format(formatoHora)
                        + ";" + actividad.getHoraFin().format(formatoHora)
                        + ";" + limpiarTexto(actividad.getDescripcion())
                        + ";" + limpiarTexto(actividad.getDatoEspecifico());

                escritor.write(linea);
                escritor.newLine();
            }
        }

        escritor.close();
    }

    private void cargarDias(SistemaAgenda sistema) throws IOException {

        BufferedReader lector = new BufferedReader(new FileReader(rutaDias));

        String linea = lector.readLine();

        while ((linea = lector.readLine()) != null) {

            if (!linea.trim().isEmpty()) {

                try {

                    LocalDate fecha = LocalDate.parse(linea, formatoFecha);

                    if (sistema.buscarDia(fecha) == null) {
                        sistema.agregarDia(new DiaAgenda(fecha));
                    }

                } catch (RuntimeException error) {
                    System.out.println("Se encontro una fecha invalida en dias.csv.");
                }
            }
        }

        lector.close();
    }

    private void cargarActividades(SistemaAgenda sistema) throws IOException {

        BufferedReader lector = new BufferedReader(new FileReader(rutaActividades));

        String linea = lector.readLine();
        int mayorId = 0;

        while ((linea = lector.readLine()) != null) {

            if (!linea.trim().isEmpty()) {

                String[] datos = linea.split(";", -1);

                if (datos.length != 8) {
                    System.out.println("Se encontro una actividad invalida en actividades.csv.");
                    continue;
                }

                try {

                    LocalDate fecha = LocalDate.parse(datos[0], formatoFecha);
                    int id = Integer.parseInt(datos[1]);
                    String tipoActividad = datos[2];
                    String titulo = datos[3];
                    LocalTime horaInicio = LocalTime.parse(datos[4], formatoHora);
                    LocalTime horaFin = LocalTime.parse(datos[5], formatoHora);
                    String descripcion = datos[6];
                    String datoEspecifico = datos[7];

                    Actividad actividad = crearActividad(
                            id,
                            tipoActividad,
                            titulo,
                            horaInicio,
                            horaFin,
                            descripcion,
                            datoEspecifico
                    );

                    if (actividad != null) {

                        sistema.agregarActividad(fecha, actividad);

                        if (id > mayorId) {
                            mayorId = id;
                        }
                    }

                } catch (RuntimeException error) {
                    System.out.println("Se encontro una actividad invalida en actividades.csv.");
                }
            }
        }

        lector.close();

        sistema.setSiguienteId(mayorId + 1);
    }

    private Actividad crearActividad(int id, String tipoActividad, String titulo,
                                     LocalTime horaInicio, LocalTime horaFin,
                                     String descripcion, String datoEspecifico) {

        if (tipoActividad.equalsIgnoreCase("Academica")) {

            return new ActividadAcademica(
                    id,
                    titulo,
                    horaInicio,
                    horaFin,
                    descripcion,
                    datoEspecifico
            );

        } else if (tipoActividad.equalsIgnoreCase("Proyecto")) {

            return new ActividadProyecto(
                    id,
                    titulo,
                    horaInicio,
                    horaFin,
                    descripcion,
                    datoEspecifico
            );

        } else if (tipoActividad.equalsIgnoreCase("Personal")) {

            return new ActividadPersonal(
                    id,
                    titulo,
                    horaInicio,
                    horaFin,
                    descripcion,
                    datoEspecifico
            );

        } else if (tipoActividad.equalsIgnoreCase("Otro")) {

            return new ActividadOtro(
                    id,
                    titulo,
                    horaInicio,
                    horaFin,
                    descripcion,
                    datoEspecifico
            );
        }

        return null;
    }

    private String limpiarTexto(String texto) {

        if (texto == null) {
            return "";
        }

        return texto.replace(";", ",")
                .replace("\n", " ")
                .replace("\r", " ");
    }
}
