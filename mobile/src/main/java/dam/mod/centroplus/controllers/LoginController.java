package dam.mod.centroplus.controllers;

import dam.mod.centroplus.models.Usuario;
import dam.mod.centroplus.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.centroplus.repositories.impl.UsuarioRepository;
import dam.mod.centroplus.services.IUsuarioService;
import dam.mod.centroplus.services.impl.UsuarioServiceImpl;
import dam.mod.centroplus.utils.LanguageManager;
import dam.mod.centroplus.utils.PasswordUtils;
import dam.mod.centroplus.utils.ScreenManager;
import dam.mod.centroplus.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ResourceBundle;

/**
 * Controlador de login y registro.
 */
public class LoginController {

    private final IUsuarioService service =
            new UsuarioServiceImpl(
                    new UsuarioRepository(),
                    new RememberTokenRepositoryImpl()
            );

    @FXML private TextField dniField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML private TextField nombreField;
    @FXML private TextField dniRegisterField;
    @FXML private TextField emailField;
    @FXML private TextField telefonoField;
    @FXML private PasswordField registerPasswordField;
    @FXML private CheckBox rememberMeCheckBox;
    @FXML private Label registerErrorLabel;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {

        bundle = LanguageManager.getBundle();

        if (bundle == null) {
            bundle = ResourceBundle.getBundle(
                    "i18n.messages",
                    LanguageManager.getLocale()
            );
        }

        String token = Session.getTokenSesionGuardado();

        if (token != null && !token.isBlank()) {
            Usuario usuario = service.autoLogin();

            if (usuario != null) {
                Session.setCurrentUser(usuario);
                ScreenManager.change("inicio.fxml");
            }
        }
    }

    @FXML
    private void handleLogin() {

        try {
            Usuario usuario = service.login(
                    dniField.getText().trim().toUpperCase(),
                    passwordField.getText(),
                    rememberMeCheckBox.isSelected()
            );

            Session.setCurrentUser(usuario);
            ScreenManager.change("inicio.fxml");

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

@FXML
private void handleRegister() {

    try {
        String pass = registerPasswordField.getText();

        if (pass == null || pass.trim().length() < 6) {
            registerErrorLabel.setText(bundle.getString("error.password.short"));
            return;
        }

        Usuario nuevo = new Usuario(
                0,
                nombreField.getText(),
                dniRegisterField.getText().trim().toUpperCase(),
                emailField.getText(),
                telefonoField.getText(),
                "ALUMNO",
                pass
        );

        service.create(nuevo);
        ScreenManager.change("login.fxml");

    } catch (Exception e) {
        registerErrorLabel.setText(bundle.getString("error.register"));
    }
}

    @FXML
    private void abrirRegistro() {
        ScreenManager.change("register.fxml");
    }

    @FXML
    private void volverLogin() {
        ScreenManager.change("login.fxml");
    }

    @FXML
    private void setSpanish() {
        aplicarIdioma("es");
    }

    @FXML
    private void setEnglish() {
        aplicarIdioma("en");
    }

    @FXML
    private void setGerman() {
        aplicarIdioma("de");
    }

    private void aplicarIdioma(String lang) {

        LanguageManager.setLanguage(lang);

        ScreenManager.change(ScreenManager.getCurrentScreen());
    }
}