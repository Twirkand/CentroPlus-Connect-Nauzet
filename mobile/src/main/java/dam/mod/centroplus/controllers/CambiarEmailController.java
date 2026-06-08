package dam.mod.centroplus.controllers;

import dam.mod.centroplus.models.Usuario;
import dam.mod.centroplus.repositories.IUsuarioRepository;
import dam.mod.centroplus.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.centroplus.repositories.impl.UsuarioRepository;
import dam.mod.centroplus.services.IUsuarioService;
import dam.mod.centroplus.services.impl.UsuarioServiceImpl;
import dam.mod.centroplus.utils.LanguageManager;
import dam.mod.centroplus.utils.ScreenManager;
import dam.mod.centroplus.utils.Session;
import dam.mod.centroplus.utils.Validaciones;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;

/**
 * Controlador para cambiar el email del usuario.
 */
public class CambiarEmailController {

    @FXML
    private Label mensajeLabel;

    @FXML
    private TextField emailField;

    @FXML
    private TextField repeatEmailField;

    private IUsuarioService usuarioService;
    private ResourceBundle bundle;

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        bundle = LanguageManager.getBundle();

        IUsuarioRepository repo = new UsuarioRepository();
        usuarioService = new UsuarioServiceImpl(repo, new RememberTokenRepositoryImpl());

        emailField.clear();
        repeatEmailField.clear();
    }

    @FXML
    private void cambiarEmail() {

        Usuario user = Session.getCurrentUser();

        String email = emailField.getText();
        String repeat = repeatEmailField.getText();

        if (email == null || email.isBlank() || repeat == null || repeat.isBlank()) {
            mensajeLabel.setText(bundle.getString("error.fields.required"));
            return;
        }

        if (!email.equals(repeat)) {
            mensajeLabel.setText(bundle.getString("error.email.mismatch"));
            return;
        }

        try {
            Validaciones.validarEmail(email);

            user.setEmail(email);
            usuarioService.update(user);

            Session.setCurrentUser(user);
            ScreenManager.change("perfil.fxml");

        } catch (IllegalArgumentException e) {
            mensajeLabel.setText(bundle.getString("error.email.invalid"));
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("perfil.fxml");
    }
}