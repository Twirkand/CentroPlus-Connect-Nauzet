package dam.mod.centroplus.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para el manejo seguro de contraseñas.
 *
 * Documentación oficial de BCrypt:
 * https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/crypto/bcrypt/BCrypt.html
 */
public class PasswordUtils {

    /**
     * Genera un hash seguro a partir de una contraseña en texto plano.
     *
     * @param plainPassword contraseña en texto plano
     * @return contraseña cifrada (hash BCrypt)
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Comprueba si una contraseña en texto plano coincide con su hash.
     *
     * @param plainPassword contraseña introducida por el usuario
     * @param hashedPassword contraseña almacenada (hash BCrypt)
     * @return true si coincide, false en caso contrario
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}