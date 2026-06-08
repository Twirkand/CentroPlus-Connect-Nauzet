package dam.mod.centroplus.utils;
 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dam.mod.centroplus.utils.TokenUtils;

import static org.junit.jupiter.api.Assertions.*;
 
@DisplayName("Tests de TokenUtils")
class TokenUtilsTest {
 
    private static final String HELLO_SHA256 =
            "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824";
 
    @Test
    @DisplayName("sha256 de 'hello' devuelve el hash conocido")
    void sha256_valorConocido() {
        assertEquals(HELLO_SHA256, TokenUtils.sha256("hello"));
    }
 
    @Test
    @DisplayName("sha256 devuelve siempre 64 caracteres hexadecimales")
    void sha256_longitud64() {
        assertEquals(64, TokenUtils.sha256("cualquier texto").length());
    }
 
    @Test
    @DisplayName("sha256 es determinista: misma entrada, misma salida")
    void sha256_determinista() {
        String token = "mi-token-secreto";
        assertEquals(TokenUtils.sha256(token), TokenUtils.sha256(token));
    }
 
    @Test
    @DisplayName("sha256 de entradas distintas produce hashes distintos")
    void sha256_entradasDistintas_hashesDistintos() {
        assertNotEquals(TokenUtils.sha256("tokenA"), TokenUtils.sha256("tokenB"));
    }
 
    @Test
    @DisplayName("sha256 solo contiene caracteres hexadecimales")
    void sha256_soloHex() {
        String hash = TokenUtils.sha256("test");
        assertTrue(hash.matches("[0-9a-f]+"));
    }
 
    @Test
    @DisplayName("sha256 de cadena vacía no lanza excepción")
    void sha256_cadenaVacia() {
        assertDoesNotThrow(() -> TokenUtils.sha256(""));
    }
 
    @Test
    @DisplayName("sha256 de cadena vacía devuelve 64 caracteres")
    void sha256_cadenaVacia_longitud64() {
        assertEquals(64, TokenUtils.sha256("").length());
    }
}