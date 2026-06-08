package dam.mod.centroplus.repositories;
 
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
 
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import dam.mod.centroplus.models.Actividad;
import dam.mod.centroplus.repositories.impl.ActividadRepository;
import dam.mod.centroplus.repositories.sqlite.ConnectionManager;
 
@ExtendWith(MockitoExtension.class)
public class ActividadRepositoryTest {
 
    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;
 
    ActividadRepository repository;
 
    final int    id         = 1;
    final String nombre     = "Yoga";
    final String tipo       = "DEPORTIVA";
    final int    duracion   = 60;
    final double precio     = 15.0;
    final int    plazasMax  = 20;
    final int    plazasOcup = 5;
 
    @BeforeEach
    void setup() {
        repository = new ActividadRepository();
    }
 
    private void configurarResultSetConUnaFila() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getString("nombre")).thenReturn(nombre);
        when(resultSet.getString("tipo_actividad")).thenReturn(tipo);
        when(resultSet.getInt("duracion")).thenReturn(duracion);
        when(resultSet.getDouble("precio")).thenReturn(precio);
        when(resultSet.getInt("plazas_maximas")).thenReturn(plazasMax);
        when(resultSet.getInt("plazas_ocupadas")).thenReturn(plazasOcup);
    }
 
    @DisplayName("findAll: devuelve lista con una actividad y mapea todos los campos")
    @Order(1)
    @Test
    void findAllDevuelveListaTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            List<Actividad> resultado = repository.findAll();
 
            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(1,         resultado.size()),
                    () -> Assertions.assertEquals(id,        resultado.get(0).getId()),
                    () -> Assertions.assertEquals(nombre,    resultado.get(0).getNombre()),
                    () -> Assertions.assertEquals(tipo,      resultado.get(0).getTipoActividad()),
                    () -> Assertions.assertEquals(duracion,  resultado.get(0).getDuracion()),
                    () -> Assertions.assertEquals(precio,    resultado.get(0).getPrecio(), 0.001),
                    () -> Assertions.assertEquals(plazasMax, resultado.get(0).getPlazasMaximas()),
                    () -> Assertions.assertEquals(plazasOcup,resultado.get(0).getPlazasOcupadas())
            );
        }
    }
 
    @DisplayName("findAll: lista vacía cuando no hay filas")
    @Order(2)
    @Test
    void findAllListaVaciaTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertTrue(repository.findAll().isEmpty(),
                    "Sin filas debe devolver lista vacía");
        }
    }
 
    @DisplayName("findAll: lanza RuntimeException si falla la BD")
    @Order(3)
    @Test
    void findAllExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertThrows(RuntimeException.class, () -> repository.findAll());
        }
    }
 
    @DisplayName("findById: devuelve actividad cuando existe y mapea todos los campos")
    @Order(4)
    @Test
    void findByIdEncontradoTest() throws SQLException {
        configurarResultSetConUnaFila();
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Actividad resultado = repository.findById(id);
 
            Assertions.assertAll(
                    () -> Assertions.assertNotNull(resultado),
                    () -> Assertions.assertEquals(id,        resultado.getId()),
                    () -> Assertions.assertEquals(nombre,    resultado.getNombre()),
                    () -> Assertions.assertEquals(tipo,      resultado.getTipoActividad()),
                    () -> Assertions.assertEquals(duracion,  resultado.getDuracion()),
                    () -> Assertions.assertEquals(precio,    resultado.getPrecio(), 0.001),
                    () -> Assertions.assertEquals(plazasMax, resultado.getPlazasMaximas()),
                    () -> Assertions.assertEquals(plazasOcup,resultado.getPlazasOcupadas())
            );
        }
    }
 
    @DisplayName("findById: verifica que se setea el id como parámetro")
    @Order(5)
    @Test
    void findByIdVerificaParametroTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            repository.findById(id);
 
            verify(preparedStatement).setInt(1, id);
        }
    }
 
    @DisplayName("findById: devuelve null cuando no existe")
    @Order(6)
    @Test
    void findByIdNoEncontradoTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertNull(repository.findById(99),
                    "Si no existe debe devolver null");
        }
    }
 
    @DisplayName("findById: lanza RuntimeException si falla la BD")
    @Order(7)
    @Test
    void findByIdExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertThrows(RuntimeException.class, () -> repository.findById(id));
        }
    }
 
    @DisplayName("save: devuelve true cuando inserta correctamente")
    @Order(8)
    @Test
    void saveTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertTrue(repository.save(
                    new Actividad(0, nombre, tipo, duracion, precio, plazasMax, plazasOcup)));
        }
    }
 
    @DisplayName("save: verifica que se setean todos los parámetros en orden correcto")
    @Order(9)
    @Test
    void saveVerificaParametrosTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            repository.save(new Actividad(0, nombre, tipo, duracion, precio, plazasMax, plazasOcup));
 
            verify(preparedStatement).setString(1, nombre);
            verify(preparedStatement).setString(2, tipo);
            verify(preparedStatement).setInt(3, duracion);
            verify(preparedStatement).setDouble(4, precio);
            verify(preparedStatement).setInt(5, plazasMax);
            verify(preparedStatement).setInt(6, plazasOcup);
        }
    }
 
    @DisplayName("save: devuelve false cuando no inserta ninguna fila")
    @Order(10)
    @Test
    void saveFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertFalse(repository.save(
                    new Actividad(0, nombre, tipo, duracion, precio, plazasMax, plazasOcup)));
        }
    }
 
    @DisplayName("save: lanza RuntimeException si falla la BD")
    @Order(11)
    @Test
    void saveExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertThrows(RuntimeException.class, () -> repository.save(
                    new Actividad(0, nombre, tipo, duracion, precio, plazasMax, plazasOcup)));
        }
    }
 
    @DisplayName("update: devuelve true cuando actualiza correctamente")
    @Order(12)
    @Test
    void updateTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertTrue(repository.update(
                    new Actividad(id, nombre, tipo, duracion, precio, plazasMax, plazasOcup)));
        }
    }
 
    @DisplayName("update: verifica que el id se setea en la posición 7 (última)")
    @Order(13)
    @Test
    void updateVerificaIdEnPosicion7Test() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            repository.update(new Actividad(id, nombre, tipo, duracion, precio, plazasMax, plazasOcup));
 
            verify(preparedStatement).setInt(7, id);
        }
    }
 
    @DisplayName("update: devuelve false cuando no encuentra la actividad")
    @Order(14)
    @Test
    void updateFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertFalse(repository.update(
                    new Actividad(99, nombre, tipo, duracion, precio, plazasMax, plazasOcup)));
        }
    }
 
    @DisplayName("update: lanza RuntimeException si falla la BD")
    @Order(15)
    @Test
    void updateExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertThrows(RuntimeException.class, () -> repository.update(
                    new Actividad(id, nombre, tipo, duracion, precio, plazasMax, plazasOcup)));
        }
    }
 
    @DisplayName("delete: devuelve true cuando elimina correctamente")
    @Order(16)
    @Test
    void deleteTrueTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertTrue(repository.delete(id));
        }
    }
 
    @DisplayName("delete: verifica que se setea el id correcto como parámetro")
    @Order(17)
    @Test
    void deleteVerificaParametroTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            repository.delete(id);
 
            verify(preparedStatement).setInt(1, id);
        }
    }
 
    @DisplayName("delete: devuelve false cuando no encuentra la actividad")
    @Order(18)
    @Test
    void deleteFalseTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertFalse(repository.delete(99));
        }
    }
 
    @DisplayName("delete: lanza RuntimeException si falla la BD")
    @Order(19)
    @Test
    void deleteExcepcionSQLTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error BD"));
 
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection).thenReturn(connection);
 
            Assertions.assertThrows(RuntimeException.class, () -> repository.delete(id));
        }
    }
}