package dam.mod.centroplus.services;

import java.util.List;

import dam.mod.centroplus.models.Usuario;

/**
 * Interfaz que define las operaciones de negocio relacionadas con usuarios.
 */
public interface IUsuarioService {

    /**
     * Devuelve todos los usuarios registrados en el sistema.
     *
     * @return lista de usuarios
     */
    List<Usuario> findAll();

    /**
     * Busca un usuario por su identificador.
     *
     * @param id ID del usuario
     * @return usuario encontrado o null si no existe
     */
    Usuario findById(int id);

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param usuario objeto usuario a crear
     * @return true si la creación fue correcta, false en caso contrario
     */
    boolean create(Usuario usuario);

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param usuario objeto usuario con datos actualizados
     * @return true si la actualización fue correcta, false en caso contrario
     */
    boolean update(Usuario usuario);

    /**
     * Elimina un usuario por su identificador.
     *
     * @param id ID del usuario
     * @return true si el usuario fue eliminado correctamente, false en caso
     *         contrario
     */
    boolean delete(int id);

    /**
     * Autentica un usuario en el sistema mediante DNI y contraseña.
     *
     * @param dni        identificador único del usuario
     * @param password   contraseña del usuario
     * @param rememberMe Recuerdame
     * @return usuario autenticado si las credenciales son correctas, null en caso
     *         contrario
     */
    Usuario login(String dni, String password, boolean rememberMe);

    /**
     * Busca un usuario por su DNI.
     *
     * @param dni documento nacional de identidad del usuario
     * @return usuario encontrado o null si no existe
     */
    Usuario findByDni(String dni);

    /**
     * Intenta iniciar sesión automáticamente utilizando la información de autenticación almacenada previamente.
     *
     * @return El usuario autenticado si el inicio de sesión automático se realiza correctamente.
     */
    Usuario autoLogin();

    /**
     * Cierra la sesión del usuario actual.
     *
     * Elimina la información de autenticación almacenada y finaliza la sesión activa para impedir futuros accesos sin volver a iniciar sesión.
     */
    void logout();
}