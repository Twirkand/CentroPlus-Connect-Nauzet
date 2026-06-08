package dam.mod.centroplus.controllers;

import dam.mod.centroplus.models.Actividad;
import dam.mod.centroplus.repositories.IActividadRepository;
import dam.mod.centroplus.repositories.impl.ActividadRepository;
import dam.mod.centroplus.services.IActividadService;
import dam.mod.centroplus.services.impl.ActividadServiceImpl;
import dam.mod.centroplus.utils.ScreenManager;
import dam.mod.centroplus.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * Controlador de la pantalla de actividades.
 *
 * Permite listar actividades disponibles y abrir su detalle.
 */
public class ActividadesController {

    @FXML
    private ListView<Actividad> listaActividades;

    private IActividadService actividadService;

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        IActividadRepository repo = new ActividadRepository();
        actividadService = new ActividadServiceImpl(repo);

        cargarActividades();
    }

    private void cargarActividades() {

        List<Actividad> actividades = actividadService.findAll();

        listaActividades.getItems().clear();
        listaActividades.getItems().addAll(actividades);
    }

    @FXML
    private void seleccionarActividad() {

        Actividad seleccionada =
                listaActividades.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            DetalleActividadController.setActividad(seleccionada);
            ScreenManager.change("detalle_actividad.fxml");
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}