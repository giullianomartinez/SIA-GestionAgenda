import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {
        SistemaAgenda sistema = new SistemaAgenda();

        cargarDatosIniciales(sistema);
        sistema.mostrarDias();
    }

    private static void cargarDatosIniciales(SistemaAgenda sistema) {
        LocalDate fechaClase = LocalDate.of(2026, 8, 13);
        LocalDate fechaProyecto = LocalDate.of(2026, 8, 14);

        Actividad actividad1 = new Actividad(
                1,
                "Clase de Java",
                LocalTime.of(10, 0),
                LocalTime.of(11, 30),
                "Clase de Programacion Avanzada",
                "Universidad"
        );

        Actividad actividad2 = new Actividad(
                2,
                "Reunion de proyecto",
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                "Revisar avance del proyecto SIA",
                "Proyecto"
        );

        Actividad actividad3 = new Actividad(
                3,
                "Ayudantia",
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                "Resolver dudas de la asignatura",
                "Universidad"
        );

        sistema.agregarActividad(fechaClase, actividad1);
        sistema.agregarActividad(fechaClase, actividad3);
        sistema.agregarActividad(fechaProyecto, actividad2);
    }
}
