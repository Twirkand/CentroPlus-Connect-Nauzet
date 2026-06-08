package dam.mod.centroplus;

import dam.mod.centroplus.models.Usuario;
import dam.mod.centroplus.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.centroplus.repositories.impl.UsuarioRepository;
import dam.mod.centroplus.services.IUsuarioService;
import dam.mod.centroplus.services.impl.UsuarioServiceImpl;
import dam.mod.centroplus.utils.ScreenManager;
import dam.mod.centroplus.utils.Session;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación.
 * 
 * @author Alberto Carballo Hernández
 * @author Nauzet Torres Tejera
 * @version 1.0 - SNAPSHOT
 * 
 */
public class Main extends Application {

    /**
     * Método principal de inicio de la aplicación JavaFX.
     *
     * @param stage ventana principal de la aplicación
     */
    @Override
    public void start(Stage stage) {

        /**
         * Inicializa el gestor de pantallas con el Stage principal
         */
        ScreenManager.init(stage);

        /**
         * Servicio de usuarios con acceso a repositorio
         */
        IUsuarioService service = new UsuarioServiceImpl(
                new UsuarioRepository(),
                new RememberTokenRepositoryImpl()
        );

        /**
         * Registra el servicio en la sesión global de la aplicación
         */
        Session.setUsuarioService(service);

        Usuario usuario = service.autoLogin();

        if (usuario != null) {
            Session.setCurrentUser(usuario);
            ScreenManager.change("inicio.fxml");
        } else {
            ScreenManager.change("login.fxml");
        }

        /**
         * Configura el icono de la aplicación
         */
        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/icons/app.png"))
        );

        /**
         * Configuración básica de la ventana
         */
        stage.setTitle("CentroPlus-Connect");
        stage.setResizable(false);

        /**
         * Muestra la ventana principal
         */
        stage.show();
    }

    /**
     * Punto de entrada estándar de Java.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch();
    }
}