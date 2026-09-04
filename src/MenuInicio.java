import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuInicio {

    private BufferedReader lector;

    public MenuInicio() {
        this.lector = new BufferedReader(new InputStreamReader(System.in));
    }

    public BufferedReader getLector() {
        return lector;
    }
    
    public void setLector(BufferedReader lector) {
        this.lector = lector;
    }

    // Permite elegir entre modo normal, modo prueba o finalizar el programa.
    // Cada modo crea su propia instancia de SistemaAgenda.
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

                    // El modo normal inicia con una agenda vacía.
                    SistemaAgenda sistema = new SistemaAgenda();
                    MenuAgenda menu = new MenuAgenda(sistema, lector);

                    menu.iniciar();

                } else if (opcion.equals("2")) {

                    // Se crea un sistema independiente para evitar mezclar datos de prueba con datos normales.
                    // El modo prueba utiliza una agenda independiente con datos precargados.
                    SistemaAgenda sistema = new SistemaAgenda();
                    sistema.cargarDatosIniciales();

                    MenuAgenda menu = new MenuAgenda(sistema, lector);

                    menu.iniciar();

                } else if (opcion.equals("3")) {

                    continuar = false;
                    System.out.println("Programa finalizado.");

                } else {
                    System.out.println("Opcion no valida.");
                }

            } catch (IOException error) {
                System.out.println("No se pudo leer la opcion.");
            }
        }
    }
}
