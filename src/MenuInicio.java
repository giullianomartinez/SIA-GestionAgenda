import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.SwingUtilities;


public class MenuInicio {

    private BufferedReader lector;
    private SistemaAgenda sistemaNormal;
    private PersistenciaAgenda persistencia;

    public MenuInicio() {

        this.lector = new BufferedReader(new InputStreamReader(System.in));
        this.sistemaNormal = new SistemaAgenda();
        this.persistencia = new PersistenciaAgenda();

        persistencia.cargarDatos(sistemaNormal);
    }

    public SistemaAgenda getSistemaNormal() {
        return sistemaNormal;
    }

    public void setSistemaNormal(SistemaAgenda sistemaNormal) {
        this.sistemaNormal = sistemaNormal;
    }

    public PersistenciaAgenda getPersistencia() {
        return persistencia;
    }

    public void setPersistencia(PersistenciaAgenda persistencia) {
        this.persistencia = persistencia;
    }

    public BufferedReader getLector() {
        return lector;
    }

    public void setLector(BufferedReader lector) {
        this.lector = lector;
    }


    public void seleccionarInterfaz(String interfaz) throws IOException {

        System.out.println();
        System.out.println("===== SELECCIONAR MODO =====");
        System.out.println("1. Modo normal");
        System.out.println("2. Modo prueba");
        System.out.println("3. Volver");
        System.out.print("Seleccione una opcion: ");

        String opcion = lector.readLine();

        SistemaAgenda sistema;

        if (opcion.equals("1")) {

            sistema = sistemaNormal;

        } else if (opcion.equals("2")) {

            sistema = new SistemaAgenda();
            sistema.cargarDatosIniciales();

        } else if (opcion.equals("3")) {

            return;

        } else {

            System.out.println("\nOpcion no valida.");
            return;
        }

        // 1 = Consola, 2 = Ventana
        if (interfaz.equals("1")) {

            MenuAgenda menu = new MenuAgenda(sistema, lector);

            menu.iniciar();

        } else if (interfaz.equals("2")) {

            SwingUtilities.invokeLater(() -> {

                VentanaAgenda ventana = new VentanaAgenda(sistema);

                ventana.mostrarVentana();
            });
        }
    }


    // Permite elegir entre consola, ventana o finalizar el programa.
    public void iniciar() {

        boolean continuar = true;

        while (continuar) {

            System.out.println();
            System.out.println("===== SISTEMA DE AGENDA =====");
            System.out.println("1. Modo Consola");
            System.out.println("2. Modo Ventana");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");

            try {

                String opcion = lector.readLine();

                if (opcion.equals("1")) {

                    seleccionarInterfaz("1");

                } else if (opcion.equals("2")) {

                    seleccionarInterfaz("2");

                } else if (opcion.equals("3")) {

                    persistencia.guardarDatos(sistemaNormal);

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
