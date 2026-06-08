package dam.mod.centroplus.models;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dam.mod.centroplus.models.RememberToken;

import static org.junit.jupiter.api.Assertions.*;
 
@DisplayName("Tests de RememberToken")
class RememberTokenTest {
 
    private RememberToken token;
 
    @BeforeEach
    void setUp() {
        token = new RememberToken(1, 42, "hashedToken123", "2025-12-31 00:00:00");
    }
 
    @Test
    @DisplayName("Constructor completo asigna todos los campos correctamente")
    void constructorCompleto_asignaCampos() {
        assertEquals(1, token.getId());
        assertEquals(42, token.getUserId());
        assertEquals("hashedToken123", token.getTokenHash());
        assertEquals("2025-12-31 00:00:00", token.getExpiresAt());
    }
 
    @DisplayName("Constructor vacío crea objeto con valores por defecto")
    void constructorVacio_valoresPorDefecto() {
        RememberToken empty = new RememberToken();
        assertEquals(0, empty.getId());
        assertEquals(0, empty.getUserId());
        assertNull(empty.getTokenHash());
        assertNull(empty.getExpiresAt());
    }
 
    @Test
    @DisplayName("setId y getId funcionan correctamente")
    void setId_getId() {
        token.setId(99);
        assertEquals(99, token.getId());
    }
 
    @Test
    @DisplayName("setUserId y getUserId funcionan correctamente")
    void setUserId_getUserId() {
        token.setUserId(10);
        assertEquals(10, token.getUserId());
    }
 
    @Test
    @DisplayName("setTokenHash y getTokenHash funcionan correctamente")
    void setTokenHash_getTokenHash() {
        token.setTokenHash("nuevoHash");
        assertEquals("nuevoHash", token.getTokenHash());
    }
 
    @Test
    @DisplayName("setExpiresAt y getExpiresAt funcionan correctamente")
    void setExpiresAt_getExpiresAt() {
        token.setExpiresAt("2026-01-01 00:00:00");
        assertEquals("2026-01-01 00:00:00", token.getExpiresAt());
    }
 
    @Test
    @DisplayName("setTokenHash acepta null")
    void setTokenHash_null() {
        token.setTokenHash(null);
        assertNull(token.getTokenHash());
    }
 
    @Test
    @DisplayName("setExpiresAt acepta null")
    void setExpiresAt_null() {
        token.setExpiresAt(null);
        assertNull(token.getExpiresAt());
    }
 
    @Test
    @DisplayName("setId acepta valor cero")
    void setId_cero() {
        token.setId(0);
        assertEquals(0, token.getId());
    }
 
    @Test
    @DisplayName("setId acepta valor negativo")
    void setId_negativo() {
        token.setId(-1);
        assertEquals(-1, token.getId());
    }
}
