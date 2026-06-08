package dam.mod.centroplus.utils;

import java.security.MessageDigest;

/**
 * Clase de utilidad encargada de generar hashes SHA-256 a partir de un token.
 * 
 * SHA-256 es una función criptográfica de resumen que transforma una cadena
 * de texto en una secuencia única de 256 bits representada en formato
 * hexadecimal.
 * 
 * Documentación oficial:
 * https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/security/MessageDigest.html
 *
 */
public class TokenUtils {

    /**
     * Genera el hash SHA-256 de un token recibido como parámetro.
     *
     * @param tokenOriginal Cadena de texto que se desea convertir a un hash.
     * @return Hash SHA-256 del token en formato hexadecimal.
     * @throws RuntimeException Se lanza si ocurre algún error al obtener o utilizar el algoritmo SHA-256.
     */
    public static String sha256(String tokenOriginal) {
        try {

            /**
             * Obtiene una instancia de MessageDigest configurada para utilizar
             * el algoritmo criptográfico SHA-256.
             */
            MessageDigest generadorHash = MessageDigest.getInstance("SHA-256");

            /**
             * Convierte el token a un array de bytes y calcula su hash SHA-256.
             * El resultado es un array de 32 bytes (256 bits).
             */
            byte[] bytesHash = generadorHash.digest(tokenOriginal.getBytes());

            /**
             * Almacenará la representación hexadecimal del hash generado.
             */
            StringBuilder hashHexadecimal = new StringBuilder();

            /**
             * Recorre cada byte del hash y lo convierte a dos caracteres
             * hexadecimales para obtener una representación legible.
             */
            for (byte byteHash : bytesHash) {
                hashHexadecimal.append(String.format("%02x", byteHash));
            }

            /**
             * Devuelve el hash completo en formato hexadecimal.
             */
            return hashHexadecimal.toString();

        } catch (Exception excepcion) {

            /**
             * Si ocurre cualquier error durante la generación del hash,
             * se encapsula en una RuntimeException para facilitar su gestión.
             */
            throw new RuntimeException(
                    "Error generando el hash SHA-256 del token", excepcion);
        }
    }
}