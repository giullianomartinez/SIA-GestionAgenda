import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuInicio {

    private BufferedReader lector;
    private SistemaAgenda sistemaNormal;

    public MenuInicio() {
        this.lector = new BufferedReader(new InputStreamReader(System.in));
        this.sistemaNormal = new SistemaAgenda();
    }

    public BufferedReader getLector() {
        return lector;
    }

    public void setLector(BufferedReader lector) {
        this.lector = lector;
    }

    public SistemaAgenda getSistemaNormal() {
        return sistemaNormal;
    }

    public void setSistemaNormal(SistemaAgenda sistemaNormal) {
        this.sistemaNormal = sistemaNormal;
    }

    // Permite elegir entre modo normal, modo prueba o finalizar el programa.
    // El modo normal conserva sus datos mientras el programa este ejecutandose.
    public void iniciar() {

        boolean continuar = true;

        while (continuar) {

            System.out.println();
            System.out.println("===== SISTEMA DE AGENDA =====");
            System.out.println("1. Modo normal");
            System.out.println("2. Modo prueba");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                String opcion = lector.readLine();

                if (opcion.equals("1")) {

                    MenuAgenda menu = new MenuAgenda(sistemaNormal, lector);
                    menu.iniciar();

                } else if (opcion.equals("2")) {

                    // El modo prueba usa una agenda independiente con datos precargados.
                    SistemaAgenda sistemaPrueba = new SistemaAgenda();
                    sistemaPrueba.cargarDatosIniciales();

                    MenuAgenda menu = new MenuAgenda(sistemaPrueba, lector);
                    menu.iniciar();

                } else if (opcion.equals("3")) {

                    continuar = false;
                    System.out.println("Programa finalizado.");

                } else {
                    System.out.println("\nOpcion no valida.");
                }

            } catch (IOException error) {
                System.out.println("\nNo se pudo leer la opcion.");
            }
        }
    }
}