package dam.mod.centroplus.repositories;
 
import dam.mod.centroplus.models.RememberToken;
import dam.mod.centroplus.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.centroplus.repositories.sqlite.ConnectionManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de RememberTokenRepositoryImpl")
class RememberTokenRepositoryTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;
 
    RememberTokenRepositoryImpl repository;
 
    final int    id        = 1;
    final int    userId    = 42;
    final String tokenHash = "hash_abc123";
    final String expiresAt = "2099-12-31 00:00:00";
 
    @BeforeEach
    void setUp() {
        repository = new RememberTokenRepositoryImpl();
    }
 
    private void configurarResultSetConUnToken() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getInt("user_id")).thenReturn(userId);
        when(resultSet.getString("token_hash")).thenReturn(tokenHash);
        when(resultSet.getString("expires_at")).thenReturn(expiresAt);
    }
 
    @Test
    @Order(1)
    @DisplayName("saveToken: devuelve true cuando el INSERT tiene éxito")
    void saveToken_exito() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            assertTrue(repository.saveToken(userId, tokenHash, expiresAt));
        }
    }
 
    @Test
    @Order(2)
    @DisplayName("saveToken: devuelve false cuando el INSERT no afecta ninguna fila")
    void saveToken_fallo() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            assertFalse(repository.saveToken(userId, tokenHash, expiresAt));
        }
    }
 
    @Test
    @Order(3)
    @DisplayName("saveToken: lanza RuntimeException ante error de BD")
    void saveToken_excepcion() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> repository.saveToken(userId, tokenHash, expiresAt));
            assertTrue(ex.getMessage().contains("Error guardando token"));
        }
    }
 
    @Test
    @Order(4)
    @DisplayName("saveToken: verifica que se setean los tres parámetros correctamente")
    void saveToken_verificaParametros() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            repository.saveToken(userId, tokenHash, expiresAt);
 
            verify(preparedStatement).setInt(1, userId);
            verify(preparedStatement).setString(2, tokenHash);
            verify(preparedStatement).setString(3, expiresAt);
        }
    }
 
    @Test
    @Order(5)
    @DisplayName("findByHash: devuelve el token cuando existe y no ha expirado")
    void findByHash_encontrado() throws SQLException {
        configurarResultSetConUnToken();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            RememberToken result = repository.findByHash(tokenHash);
 
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(id,        result.getId()),
                    () -> assertEquals(userId,    result.getUserId()),
                    () -> assertEquals(tokenHash, result.getTokenHash()),
                    () -> assertEquals(expiresAt, result.getExpiresAt())
            );
        }
    }
 
    @Test
    @Order(6)
    @DisplayName("findByHash: devuelve null cuando el hash no existe")
    void findByHash_noEncontrado() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            assertNull(repository.findByHash("hash_inexistente"),
                    "Hash no existente debe devolver null");
        }
    }
 
    @Test
    @Order(7)
    @DisplayName("findByHash: devuelve null cuando el token ha expirado (SQL filtra por expires_at)")
    void findByHash_tokenExpirado() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            assertNull(repository.findByHash("hash_expirado"),
                    "Token expirado filtrado por la BD debe devolver null");
        }
    }
 
    @Test
    @Order(8)
    @DisplayName("findByHash: lanza RuntimeException ante error de BD")
    void findByHash_excepcion() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> repository.findByHash(tokenHash));
            assertTrue(ex.getMessage().contains("Error buscando token"));
        }
    }
 
    @Test
    @Order(9)
    @DisplayName("findByHash: verifica que se setea el hash como parámetro")
    void findByHash_verificaParametro() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            repository.findByHash(tokenHash);
 
            verify(preparedStatement).setString(1, tokenHash);
        }
    }
 
    @Test
    @Order(10)
    @DisplayName("findAllValid: devuelve lista con tokens vigentes")
    void findAllValid_conTokens() throws SQLException {
        configurarResultSetConUnToken();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            List<RememberToken> result = repository.findAllValid();
 
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(1,         result.size()),
                    () -> assertEquals(tokenHash, result.get(0).getTokenHash()),
                    () -> assertEquals(userId,    result.get(0).getUserId())
            );
        }
    }
 
    @Test
    @Order(11)
    @DisplayName("findAllValid: devuelve lista vacía cuando no hay tokens vigentes")
    void findAllValid_listaVacia() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            List<RememberToken> result = repository.findAllValid();
 
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
 
    @Test
    @Order(12)
    @DisplayName("findAllValid: lanza RuntimeException ante error de BD")
    void findAllValid_excepcion() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> repository.findAllValid());
            assertTrue(ex.getMessage().contains("Error leyendo tokens"));
        }
    }
 
    @Test
    @Order(13)
    @DisplayName("findAllValid: mapea correctamente todos los campos del token")
    void findAllValid_mapeaCamposCorrectamente() throws SQLException {
        configurarResultSetConUnToken();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            RememberToken token = repository.findAllValid().get(0);
 
            assertAll(
                    () -> assertEquals(id,        token.getId()),
                    () -> assertEquals(userId,    token.getUserId()),
                    () -> assertEquals(tokenHash, token.getTokenHash()),
                    () -> assertEquals(expiresAt, token.getExpiresAt())
            );
        }
    }
 
    @Test
    @Order(14)
    @DisplayName("deleteByUserId: devuelve true cuando borra al menos un token")
    void deleteByUserId_exito() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            assertTrue(repository.deleteByUserId(userId));
        }
    }
 
    @Test
    @Order(15)
    @DisplayName("deleteByUserId: devuelve false cuando el usuario no tiene tokens")
    void deleteByUserId_sinTokens() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            assertFalse(repository.deleteByUserId(999));
        }
    }
 
    @Test
    @Order(16)
    @DisplayName("deleteByUserId: lanza RuntimeException ante error de BD")
    void deleteByUserId_excepcion() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> repository.deleteByUserId(userId));
            assertTrue(ex.getMessage().contains("Error borrando tokens"));
        }
    }
 
    @Test
    @Order(17)
    @DisplayName("deleteByUserId: verifica que se setea el userId como parámetro")
    void deleteByUserId_verificaParametro() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            repository.deleteByUserId(userId);
 
            verify(preparedStatement).setInt(1, userId);
        }
    }
}
