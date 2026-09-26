import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class VentanaActividades extends JFrame {

    private SistemaAgenda sistema;
    private JTextArea areaResultados;
    private DateTimeFormatter formatoFecha;
    private DateTimeFormatter formatoHora;

    public VentanaActividades(SistemaAgenda sistema) {

        this.sistema = sistema;

        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {

        setTitle("Gestion de Actividades");

        setSize(750, 500);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    private void crearComponentes() {

        JLabel titulo = new JLabel("GESTION DE ACTIVIDADES", SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        add(titulo, BorderLayout.NORTH);


        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(new GridLayout(6, 1, 5, 5));


        JButton botonAgregar = new JButton("Agregar actividad");

        JButton botonListar = new JButton("Listar actividades de un dia");

        JButton botonBuscar = new JButton("Buscar actividad");

        JButton botonEditar = new JButton("Editar actividad");

        JButton botonEliminar = new JButton("Eliminar actividad");

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


        botonAgregar.addActionListener(evento -> agregarActividad());

        botonListar.addActionListener(evento -> listarActividades());

        botonBuscar.addActionListener(evento -> buscarActividad());

        botonEditar.addActionListener(evento -> editarActividad());

        botonEliminar.addActionListener(evento -> eliminarActividad());

        botonCerrar.addActionListener(evento -> dispose());

    }

    private void agregarActividad() {

        LocalDate fecha = solicitarFecha("Ingrese fecha del dia (dd/MM/yyyy):");

        if (fecha == null) {
            return;
        }

        DiaAgenda dia = sistema.buscarDia(fecha);

        if (dia == null) {

            JOptionPane.showMessageDialog(this, "El dia no esta registrado.\n" + "Primero debe agregar el dia.");

            return;
        }


        String tipo = solicitarTipoActividad();

        if (tipo == null) {
            return;
        }


        String titulo = solicitarTexto("Ingrese titulo:");

        if (titulo == null) {
            return;
        }


        LocalTime horaInicio = solicitarHora("Ingrese hora de inicio (HH:mm):");

        if (horaInicio == null) {
            return;
        }


        LocalTime horaFin = solicitarHora("Ingrese hora de fin (HH:mm):");

        if (horaFin == null) {
            return;
        }


        String descripcion = solicitarTexto("Ingrese descripcion:");

        if (descripcion == null) {
            return;
        }


        int id = sistema.generarIdActividad();

        try {

            Actividad actividad = crearActividadPorTipo(id, tipo, titulo, horaInicio, horaFin, descripcion);

            if (actividad == null) {
                return;
            }

            sistema.agregarActividad(fecha, actividad);

            JOptionPane.showMessageDialog(this, "Actividad agregada correctamente.\n" + "ID asignado: " + id);

            mostrarActividadesDia(dia);

        } catch (HorarioInvalidoException error) {

            JOptionPane.showMessageDialog(this, error.getMessage());

        }
    }

    private Actividad crearActividadPorTipo(int id, String tipo, String titulo, LocalTime horaInicio, LocalTime horaFin, String descripcion) throws HorarioInvalidoException {

        if (tipo.equals("1")) {

            String asignatura = solicitarTexto("Ingrese asignatura:");

            if (asignatura == null) {
                return null;
            }

            return new ActividadAcademica(id, titulo, horaInicio, horaFin, descripcion, asignatura);


        } else if (tipo.equals("2")) {

            String nombreProyecto = solicitarTexto("Ingrese nombre del proyecto:");

            if (nombreProyecto == null) {
                return null;
            }

            return new ActividadProyecto(id, titulo, horaInicio, horaFin, descripcion, nombreProyecto);


        } else if (tipo.equals("3")) {

            String lugar = solicitarTexto("Ingrese lugar:");

            if (lugar == null) {
                return null;
            }

            return new ActividadPersonal(id, titulo, horaInicio, horaFin, descripcion, lugar);


        } else if (tipo.equals("4")) {

            String lugar = solicitarTexto("Ingrese lugar:");

            if (lugar == null) {
                return null;
            }

            return new ActividadOtro(id, titulo, horaInicio, horaFin, descripcion, lugar);
        }

        return null;
    }

    private void listarActividades() {

        LocalDate fecha = solicitarFecha("Ingrese fecha (dd/MM/yyyy):");

        if (fecha == null) {
            return;
        }

        DiaAgenda dia = sistema.buscarDia(fecha);

        if (dia == null) {

            JOptionPane.showMessageDialog(this, "No existe un dia registrado con esa fecha.");

            return;
        }

        mostrarActividadesDia(dia);
    }

    private void mostrarActividadesDia(
            DiaAgenda dia) {

        StringBuilder texto =
                new StringBuilder();

        texto.append("Fecha: ")
                .append(
                        dia.getFecha()
                                .format(formatoFecha)
                )
                .append("\n");

        texto.append("-----------------------------\n");


        if (dia.cantidadActividades() == 0) {

            texto.append("No hay actividades registradas.");

        } else {

            for (int i = 0; i < dia.cantidadActividades(); i++) {

                Actividad actividad = dia.obtenerActividad(i);

                texto.append("ID: ").append(actividad.getId()).append(" | ").append(actividad.mostrarActividad()).append("\n");
            }
        }

        areaResultados.setText(texto.toString());

        areaResultados.setCaretPosition(0);
    }

    private void buscarActividad() {

        String opcion = JOptionPane.showInputDialog(this, "Buscar actividad por:\n" + "1. ID\n" + "2. Titulo");

        if (opcion == null) {
            return;
        }


        Actividad actividad = null;


        if (opcion.equals("1")) {

            Integer id = solicitarEntero("Ingrese ID:");

            if (id == null) {
                return;
            }

            try {

                actividad = sistema.buscarActividad(id);

            } catch (ActividadNoEncontradaException error) {

                JOptionPane.showMessageDialog(this, error.getMessage());

                return;
            }


        } else if (opcion.equals("2")) {

            String titulo = solicitarTexto("Ingrese titulo:");

            if (titulo == null) {
                return;
            }

            actividad = sistema.buscarActividad(titulo);

            if (actividad == null) {

                JOptionPane.showMessageDialog(this, "Actividad no encontrada.");

                return;
            }

        } else {

            JOptionPane.showMessageDialog(this, "Opcion no valida.");

            return;
        }


        areaResultados.setText("ACTIVIDAD ENCONTRADA\n" + "-----------------------------\n" + "ID: " + actividad.getId() + "\n" + actividad.mostrarActividad());
    }

    private void editarActividad() {

        Integer id = solicitarEntero("Ingrese ID de la actividad a editar:");

        if (id == null) {
            return;
        }


        Actividad actividad;

        try {

            actividad = sistema.buscarActividad(id);

        } catch (ActividadNoEncontradaException error) {

            JOptionPane.showMessageDialog(this, error.getMessage());

            return;
        }


        String nuevoTitulo = solicitarTexto("Ingrese nuevo titulo:");

        if (nuevoTitulo == null) {
            return;
        }


        LocalTime nuevaHoraInicio = solicitarHora("Ingrese nueva hora de inicio (HH:mm):");

        if (nuevaHoraInicio == null) {
            return;
        }


        LocalTime nuevaHoraFin = solicitarHora("Ingrese nueva hora de fin (HH:mm):");

        if (nuevaHoraFin == null) {
            return;
        }


        String nuevaDescripcion = solicitarTexto("Ingrese nueva descripcion:");

        if (nuevaDescripcion == null) {
            return;
        }


        String nuevoDato = solicitarTexto("Ingrese nueva " + actividad.getNombreDatoEspecifico() + ":");

        if (nuevoDato == null) {
            return;
        }


        try {

            actividad.actualizarHorario(nuevaHoraInicio, nuevaHoraFin);

            actividad.setTitulo(nuevoTitulo);

            actividad.setDescripcion(nuevaDescripcion);

            actividad.setDatoEspecifico(nuevoDato);


            JOptionPane.showMessageDialog(this, "Actividad editada correctamente.");


            areaResultados.setText("ID: " + actividad.getId() + "\n" + actividad.mostrarActividad());


        } catch (HorarioInvalidoException error) {

            JOptionPane.showMessageDialog(this, error.getMessage());
        }
    }

    private void eliminarActividad() {

        Integer id = solicitarEntero("Ingrese ID de la actividad a eliminar:");

        if (id == null) {
            return;
        }


        int respuesta = JOptionPane.showConfirmDialog(this, "¿Esta seguro de eliminar la actividad " + id + "?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }


        try {

            sistema.eliminarActividadPorId(id);

            JOptionPane.showMessageDialog(this, "Actividad eliminada correctamente.");

            areaResultados.setText("Actividad ID " + id + " eliminada correctamente.");


        } catch (ActividadNoEncontradaException error) {

            JOptionPane.showMessageDialog(this, error.getMessage());

        }
    }

    //Metodos auxiliares de conversion
    private LocalDate solicitarFecha(String mensaje) {

        while (true) {

            String texto = JOptionPane.showInputDialog(this, mensaje);

            if (texto == null) {
                return null;
            }

            try {

                return LocalDate.parse(texto, formatoFecha);

            } catch (DateTimeParseException error) {

                JOptionPane.showMessageDialog(this, "Fecha invalida.\n" + "Ejemplo valido: 23/08/2026");

            }
        }
    }

    private LocalTime solicitarHora(String mensaje) {

        while (true) {

            String texto = JOptionPane.showInputDialog(this, mensaje);

            if (texto == null) {
                return null;
            }

            try {

                return LocalTime.parse(texto, formatoHora);

            } catch (DateTimeParseException error) {

                JOptionPane.showMessageDialog(this, "Hora invalida.\n" + "Ejemplo valido: 14:30");
            }
        }
    }

    private String solicitarTexto(String mensaje) {

        while (true) {

            String texto = JOptionPane.showInputDialog(this, mensaje);

            if (texto == null) {
                return null;
            }

            if (!texto.trim().isEmpty()) {
                return texto;
            }

            JOptionPane.showMessageDialog(this, "El texto no puede estar vacio.");
        }
    }

    private Integer solicitarEntero(String mensaje) {

        while (true) {

            String texto = JOptionPane.showInputDialog(this, mensaje);

            if (texto == null) {
                return null;
            }

            try {

                return Integer.parseInt(texto);

            } catch (NumberFormatException error) {

                JOptionPane.showMessageDialog(this, "Debe ingresar un numero entero.");
            }
        }
    }

    private String solicitarTipoActividad() {

        while (true) {

            String tipo = JOptionPane.showInputDialog(this, "Tipo de actividad:\n" + "1. Academica\n" + "2. Proyecto\n" + "3. Personal\n" + "4. Otro");

            if (tipo == null) {
                return null;
            }

            if (tipo.equals("1") || tipo.equals("2") || tipo.equals("3") || tipo.equals("4")) {
                return tipo;
            }

            JOptionPane.showMessageDialog(this, "Tipo no valido.");
        }
    }

    public void mostrarVentana() {
        setVisible(true);
    }
}
