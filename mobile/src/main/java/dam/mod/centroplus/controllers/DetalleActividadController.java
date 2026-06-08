package dam.mod.centroplus.controllers;

import java.util.List;
import java.util.ResourceBundle;

import dam.mod.centroplus.models.Actividad;
import dam.mod.centroplus.models.Reserva;
import dam.mod.centroplus.models.Usuario;
import dam.mod.centroplus.repositories.IActividadRepository;
import dam.mod.centroplus.repositories.IReservaRepository;
import dam.mod.centroplus.repositories.IUsuarioRepository;
import dam.mod.centroplus.repositories.impl.ActividadRepository;
import dam.mod.centroplus.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.centroplus.repositories.impl.ReservaRepository;
import dam.mod.centroplus.repositories.impl.UsuarioRepository;
import dam.mod.centroplus.services.IActividadService;
import dam.mod.centroplus.services.IReservaService;
import dam.mod.centroplus.services.IUsuarioService;
import dam.mod.centroplus.services.impl.ActividadServiceImpl;
import dam.mod.centroplus.services.impl.ReservaServiceImpl;
import dam.mod.centroplus.services.impl.UsuarioServiceImpl;
import dam.mod.centroplus.utils.LanguageManager;
import dam.mod.centroplus.utils.ScreenManager;
import dam.mod.centroplus.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador del detalle de una actividad.
 */
public class DetalleActividadController {

    @FXML
    private Label mensajeLabel;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblTipo;

    @FXML
    private Label lblDuracion;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblPlazas;

    private static Actividad actividadSeleccionada;

    private IActividadService actividadService;
    private IReservaService reservaService;

    private ResourceBundle bundle;

    public static void setActividad(Actividad actividad) {
        actividadSeleccionada = actividad;
    }

    @FXML
    public void initialize() {

        Usuario user = Session.getCurrentUser();
        if (user == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        bundle = LanguageManager.getBundle();

        IActividadRepository actividadRepo = new ActividadRepository();
        actividadService = new ActividadServiceImpl(actividadRepo);

        IReservaRepository reservaRepo = new ReservaRepository();

        IUsuarioRepository usuarioRepo = new UsuarioRepository();
        IUsuarioService usuarioService = new UsuarioServiceImpl(
                usuarioRepo,
                new RememberTokenRepositoryImpl()
        );

        reservaService = new ReservaServiceImpl(
                reservaRepo,
                usuarioService,
                actividadService
        );

        if (actividadSeleccionada != null) {
            cargarDatos();
        }
    }

    private void cargarDatos() {

        lblNombre.setText(bundle.getString("activity.name") + actividadSeleccionada.getNombre());

        lblTipo.setText(bundle.getString("activity.type") + actividadSeleccionada.getTipoActividad());

        lblDuracion.setText(
                bundle.getString("activity.duration") +
                actividadSeleccionada.getDuracion() +
                bundle.getString("activity.minutes")
        );

        lblPrecio.setText(
                bundle.getString("activity.price") +
                actividadSeleccionada.getPrecio() +
                bundle.getString("activity.euro")
        );

        int disponibles = actividadSeleccionada.getPlazasMaximas()
                - actividadSeleccionada.getPlazasOcupadas();

        lblPlazas.setText(bundle.getString("activity.available") + disponibles);
    }

    @FXML
    private void reservar() {

        int usuarioId = Session.getCurrentUser().getId();
        int actividadId = actividadSeleccionada.getId();

        List<Reserva> reservasUsuario = reservaService.findByIdUsuario(usuarioId);

        for (Reserva reserva : reservasUsuario) {

            if (reserva.getIdActividad() == actividadId &&
                    "ACTIVA".equals(reserva.getEstado())) {

                mensajeLabel.setText(bundle.getString("error.already.booked"));
                return;
            }
        }

        int disponibles = actividadSeleccionada.getPlazasMaximas()
                - actividadSeleccionada.getPlazasOcupadas();

        if (disponibles <= 0) {
            mensajeLabel.setText(bundle.getString("error.no.spots"));
            return;
        }

        boolean ok = reservaService.reservar(actividadId, usuarioId);

        if (ok) {

            actividadSeleccionada = actividadService.findById(actividadId);
            cargarDatos();

            mensajeLabel.setText(bundle.getString("success.booking"));

        } else {
            mensajeLabel.setText(bundle.getString("error.booking"));
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("actividades.fxml");
    }
}