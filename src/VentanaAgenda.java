import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VentanaAgenda extends JFrame {
    private SistemaAgenda sistema;
    private JTextArea areaResultados;
    private DateTimeFormatter formatoFecha;


    public VentanaAgenda(SistemaAgenda sistema) {

        this.sistema = sistema;
        this.formatoFecha =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {

        setTitle("Sistema de Agenda");

        setSize(750, 500);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    private void crearComponentes() {

        JLabel titulo = new JLabel("SISTEMA DE AGENDA", SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        add(titulo, BorderLayout.NORTH);


        // Panel que contiene los botones
        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(new GridLayout(5, 1, 10, 10));


        JButton botonDias = new JButton("Gestionar dias");

        JButton botonActividades = new JButton("Gestionar actividades");

        JButton botonMostrarAgenda = new JButton("Mostrar agenda");

        JButton botonHorario = new JButton("Buscar horario disponible");

        JButton botonCerrar = new JButton("Cerrar");


        panelBotones.add(botonDias);
        panelBotones.add(botonActividades);
        panelBotones.add(botonMostrarAgenda);
        panelBotones.add(botonHorario);
        panelBotones.add(botonCerrar);


        add(panelBotones, BorderLayout.WEST);


        // Zona donde se mostraran los resultados
        areaResultados = new JTextArea();

        areaResultados.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaResultados);

        add(scroll, BorderLayout.CENTER);


        // Acciones de los botones
        botonMostrarAgenda.addActionListener(evento -> mostrarAgenda());

        botonCerrar.addActionListener(evento -> dispose());

        botonDias.addActionListener(evento -> {

                    VentanaDias ventanaDias = new VentanaDias(sistema);
                    ventanaDias.mostrarVentana();
                }
        );

    }

    private void mostrarAgenda() {

        StringBuilder texto = new StringBuilder();

        if (sistema.getDias().isEmpty()) {

            areaResultados.setText("La agenda no tiene dias registrados.");

            return;
        }

        for (LocalDate fecha : sistema.getDias().keySet()) {

            DiaAgenda dia = sistema.getDias().get(fecha);

            texto.append("----------------------------\n");
            texto.append("Fecha: ").append(fecha.format(formatoFecha)).append("\n");


            if (dia.cantidadActividades() == 0) {

                texto.append("No hay actividades registradas.\n");

            } else {

                for (int i = 0; i < dia.cantidadActividades(); i++) {

                    Actividad actividad = dia.obtenerActividad(i);

                    texto.append(actividad.mostrarActividad());

                    texto.append("\n");
                }
            }

            texto.append("\n");
        }

        areaResultados.setText(texto.toString());

        // Lleva el scroll nuevamente arriba
        areaResultados.setCaretPosition(0);
    }

    public void mostrarVentana() {
        setVisible(true);
    }
}

