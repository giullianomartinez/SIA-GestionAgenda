import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaDias extends JFrame {

    private SistemaAgenda sistema;
    private JTextArea areaResultados;
    private DateTimeFormatter formatoFecha;

    public VentanaDias(SistemaAgenda sistema) {

        this.sistema = sistema;
        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {

        setTitle("Gestion de Dias");

        setSize(600, 450);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    private void crearComponentes() {

        JLabel titulo = new JLabel("GESTION DE DIAS", SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        add(titulo, BorderLayout.NORTH);


        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(new GridLayout(6, 1, 5, 5));


        JButton botonAgregar = new JButton("Agregar dia");

        JButton botonListar = new JButton("Listar dias");

        JButton botonBuscar = new JButton("Buscar dia");

        JButton botonEditar = new JButton("Editar dia");

        JButton botonEliminar = new JButton("Eliminar dia");

        JButton botonCerrar = new JButton("Cerrar");


        panelBotones.add(botonAgregar);
        panelBotones.add(botonListar);
        panelBotones.add(botonBuscar);
        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonCerrar);

        add(panelBotones, BorderLayout.WEST);


        areaResultados = new JTextArea();

        areaResultados.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaResultados);

        add(scroll, BorderLayout.CENTER);

        //listeners que escuchan la accion realizada
        botonAgregar.addActionListener(evento -> agregarDia());

        botonListar.addActionListener(evento -> listarDias());

        botonBuscar.addActionListener(evento -> buscarDia());

        botonEditar.addActionListener(evento -> editarDia());

        botonEliminar.addActionListener(evento -> eliminarDia());

        botonCerrar.addActionListener(evento -> dispose());
    }

    private void agregarDia() {

        LocalDate fecha = solicitarFecha("Ingrese fecha (dd/MM/yyyy):");

        if (fecha == null) {
            return;
        }

        if (sistema.buscarDia(fecha) != null) {

            JOptionPane.showMessageDialog(this, "Ya existe un dia registrado con esa fecha.");

            return;
        }

        DiaAgenda nuevoDia = new DiaAgenda(fecha);

        sistema.agregarDia(nuevoDia);

        JOptionPane.showMessageDialog(this, "Dia agregado correctamente.");

        listarDias();
    }

    private void listarDias() {

        if (sistema.getDias().isEmpty()) {

            areaResultados.setText("No hay dias registrados.");

            return;
        }

        StringBuilder texto = new StringBuilder();

        texto.append("DIAS REGISTRADOS\n");
        texto.append("-------------------------\n");

        for (LocalDate fecha : sistema.getDias().keySet()) {

            texto.append(fecha.format(formatoFecha));

            texto.append("\n");
        }

        areaResultados.setText(texto.toString());
    }

    private void buscarDia() {

        LocalDate fecha = solicitarFecha("Ingrese fecha a buscar (dd/MM/yyyy):");

        if (fecha == null) {
            return;
        }

        DiaAgenda dia = sistema.buscarDia(fecha);

        if (dia == null) {

            JOptionPane.showMessageDialog(this, "No se encontro el dia solicitado.");

            return;
        }

        StringBuilder texto = new StringBuilder();

        texto.append("Fecha: ").append(fecha.format(formatoFecha)).append("\n\n");

        if (dia.cantidadActividades() == 0) {

            texto.append("No hay actividades registradas.");

        } else {

            for (int i = 0; i < dia.cantidadActividades(); i++) {

                Actividad actividad = dia.obtenerActividad(i);

                texto.append(actividad.mostrarActividad());

                texto.append("\n");
            }
        }

        areaResultados.setText(texto.toString());
    }

    private void editarDia() {

        LocalDate fechaActual = solicitarFecha("Ingrese fecha actual (dd/MM/yyyy):");

        if (fechaActual == null) {
            return;
        }

        LocalDate nuevaFecha = solicitarFecha("Ingrese nueva fecha (dd/MM/yyyy):");

        if (nuevaFecha == null) {
            return;
        }

        boolean editado = sistema.editarFechaDia(fechaActual, nuevaFecha);

        if (editado) {

            JOptionPane.showMessageDialog(this, "Fecha editada correctamente.");

            listarDias();

        } else {

            JOptionPane.showMessageDialog(this, "No se pudo editar la fecha.");

        }
    }

    private void eliminarDia() {

        LocalDate fecha = solicitarFecha("Ingrese fecha a eliminar (dd/MM/yyyy):");

        if (fecha == null) {
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, "¿Esta seguro de eliminar este dia?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = sistema.eliminarDia(fecha);

        if (eliminado) {

            JOptionPane.showMessageDialog(this, "Dia eliminado correctamente.");

            listarDias();

        } else {

            JOptionPane.showMessageDialog(this, "No se encontro el dia solicitado.");

        }
    }

    private LocalDate solicitarFecha(String mensaje) {

        while (true) {

            String texto = JOptionPane.showInputDialog(this, mensaje);

            // El usuario presiono Cancelar
            if (texto == null) {
                return null;
            }

            try {

                return LocalDate.parse(texto, formatoFecha);

            } catch (DateTimeParseException error) {

                JOptionPane.showMessageDialog(this, "Fecha invalida. Ejemplo valido: 23/08/2026");

            }
        }
    }

    public void mostrarVentana() {
        setVisible(true);
    }

}

