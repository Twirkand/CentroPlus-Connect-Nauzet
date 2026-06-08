package dam.mod.centroplus.controllers;

import dam.mod.centroplus.models.Reserva;
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
import javafx.scene.control.ListView;

import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de reservas del usuario.
 */
public class ReservasController {

    @FXML
    private Label mensajeLabel;

    @FXML
    private ListView<Reserva> listaReservas;

    private IReservaService reservaService;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        bundle = LanguageManager.getBundle();

        IReservaRepository reservaRepo = new ReservaRepository();
        IUsuarioRepository usuarioRepo = new UsuarioRepository();
        IActividadRepository actividadRepo = new ActividadRepository();

        IUsuarioService usuarioService = new UsuarioServiceImpl(
                usuarioRepo,
                new RememberTokenRepositoryImpl()
        );

        IActividadService actividadService = new ActividadServiceImpl(actividadRepo);

        reservaService = new ReservaServiceImpl(
                reservaRepo,
                usuarioService,
                actividadService
        );

        cargarReservas();
    }

    private void cargarReservas() {

        int idUsuario = Session.getCurrentUser().getId();

        listaReservas.getItems().setAll(
                reservaService.findByIdUsuario(idUsuario)
        );
    }

    @FXML
    private void cancelarReserva() {

        Reserva seleccionada = listaReservas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) return;

        int idUsuario = Session.getCurrentUser().getId();

        if (seleccionada.getIdUsuario() != idUsuario) {
            mensajeLabel.setText(bundle.getString("error.reservation.permission"));
            return;
        }

        boolean ok = reservaService.cambiarEstado(
                seleccionada.getId(),
                "CANCELADA"
        );

        if (ok) {
            mensajeLabel.setText(bundle.getString("success.reservation.cancelled"));
            cargarReservas();
        } else {
            mensajeLabel.setText(bundle.getString("error.reservation.cancel"));
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}