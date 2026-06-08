package dam.mod.centroplus.repositories.sqlite;
 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import dam.mod.centroplus.repositories.sqlite.ConnectionManager;
import dam.mod.centroplus.repositories.sqlite.DatabaseInitializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
@DisplayName("Tests de DatabaseInitializer")
class DatabaseInitializerTest {

 
    private static final String URL_MEMORIA_NOMBRADA =
            "jdbc:sqlite:file:testdb?mode=memory&cache=shared";

    private Connection abrirConexion() throws SQLException {
        return DriverManager.getConnection(URL_MEMORIA_NOMBRADA);
    }
 
   @Test
@Order(1)
@DisplayName("inicializar: se ejecuta sin lanzar excepciones")
void inicializar_noLanzaExcepcion() throws SQLException {
    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
        mock.when(ConnectionManager::getConnection)
                .thenAnswer(inv -> abrirConexion());

        assertDoesNotThrow(DatabaseInitializer::inicializar);
    }
}

@Test
@Order(2)
@DisplayName("inicializar: es idempotente (segunda llamada no lanza excepción)")
void inicializar_idempotente() throws SQLException {
    try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
        mock.when(ConnectionManager::getConnection)
                .thenAnswer(inv -> abrirConexion());

        assertDoesNotThrow(() -> {
            DatabaseInitializer.inicializar();
            DatabaseInitializer.inicializar();
        });
    }
}
 
    @Test
    @Order(3)
    @DisplayName("inicializar: crea la tabla usuarios")
    void inicializar_creaTablaUsuarios() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            assertTrue(existeTabla(ancla, "usuarios"),
                    "La tabla 'usuarios' debe existir");
        }
    }
 
    @Test
    @Order(4)
    @DisplayName("inicializar: crea la tabla actividades")
    void inicializar_creaTablaActividades() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            assertTrue(existeTabla(ancla, "actividades"),
                    "La tabla 'actividades' debe existir");
        }
    }
 
    @Test
    @Order(5)
    @DisplayName("inicializar: crea la tabla reservas")
    void inicializar_creaTablaReservas() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            assertTrue(existeTabla(ancla, "reservas"),
                    "La tabla 'reservas' debe existir");
        }
    }
 
    @Test
    @Order(6)
    @DisplayName("inicializar: crea la tabla incidencias")
    void inicializar_creaTablaIncidencias() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            assertTrue(existeTabla(ancla, "incidencias"),
                    "La tabla 'incidencias' debe existir");
        }
    }
 
    @Test
    @Order(7)
    @DisplayName("inicializar: crea la tabla remember_tokens")
    void inicializar_creaTablaRememberTokens() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            assertTrue(existeTabla(ancla, "remember_tokens"),
                    "La tabla 'remember_tokens' debe existir");
        }
    }
 
    @Test
    @Order(8)
    @DisplayName("inicializar: crea la tabla audit_log")
    void inicializar_creaTablaAuditLog() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            assertTrue(existeTabla(ancla, "audit_log"),
                    "La tabla 'audit_log' debe existir");
        }
    }
 
    @Test
    @Order(9)
    @DisplayName("inicializar: crea las cuatro tablas _deleted de auditoría de borrado")
    void inicializar_creaTablasBorradoAuditoria() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            assertAll(
                    () -> assertTrue(existeTabla(ancla, "usuarios_deleted"),
                            "Debe existir 'usuarios_deleted'"),
                    () -> assertTrue(existeTabla(ancla, "actividades_deleted"),
                            "Debe existir 'actividades_deleted'"),
                    () -> assertTrue(existeTabla(ancla, "reservas_deleted"),
                            "Debe existir 'reservas_deleted'"),
                    () -> assertTrue(existeTabla(ancla, "incidencias_deleted"),
                            "Debe existir 'incidencias_deleted'")
            );
        }
    }
 
    @Test
    @Order(10)
    @DisplayName("inicializar: la tabla usuarios tiene la columna dni con restricción UNIQUE")
    void inicializar_usuariosTieneDniUnico() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            ancla.createStatement().executeUpdate(
                    "INSERT INTO usuarios(nombre,dni,email,tipo_usuario,password) " +
                    "VALUES('A','11111111A','a@a.com','ALUMNO','hash')");
 
            assertThrows(SQLException.class, () ->
                    ancla.createStatement().executeUpdate(
                            "INSERT INTO usuarios(nombre,dni,email,tipo_usuario,password) " +
                            "VALUES('B','11111111A','b@b.com','ALUMNO','hash')"),
                    "El DNI debe ser UNIQUE en la tabla usuarios");
        }
    }
 
    @Test
    @Order(11)
    @DisplayName("inicializar: la tabla reservas rechaza un estado no permitido")
    void inicializar_reservasCheckEstado() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            ancla.createStatement().executeUpdate(
                    "INSERT INTO usuarios(nombre,dni,email,tipo_usuario,password) " +
                    "VALUES('U','33333333C','u@u.com','ALUMNO','hash')");
            ancla.createStatement().executeUpdate(
                    "INSERT INTO actividades(nombre,tipo_actividad,duracion,precio,plazas_maximas) " +
                    "VALUES('Yoga','DEPORTIVA',60,10.0,20)");
 
            assertThrows(SQLException.class, () ->
                    ancla.createStatement().executeUpdate(
                            "INSERT INTO reservas(id_usuario,id_actividad,fecha,estado) " +
                            "VALUES(1,1,'2025-01-01','INVALIDO')"),
                    "La tabla reservas debe rechazar estados no permitidos");
        }
    }
 
    @Test
    @Order(12)
    @DisplayName("inicializar: la tabla actividades rechaza un precio negativo")
    void inicializar_actividadesCheckPrecioNegativo() throws SQLException {
        try (Connection ancla = abrirConexion();
             MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
 
            mock.when(ConnectionManager::getConnection)
                    .thenAnswer(inv -> abrirConexion());
            DatabaseInitializer.inicializar();
 
            assertThrows(SQLException.class, () ->
                    ancla.createStatement().executeUpdate(
                            "INSERT INTO actividades(nombre,tipo_actividad,duracion,precio,plazas_maximas) " +
                            "VALUES('Yoga','DEPORTIVA',60,-1.0,20)"),
                    "La tabla actividades debe rechazar precio negativo");
        }
    }
 
    @Test
    @Order(13)
    @DisplayName("inicializar: lanza RuntimeException si la conexión falla")
    void inicializar_lanzaRuntimeExceptionSiFallaBD() {
        try (MockedStatic<ConnectionManager> mock = mockStatic(ConnectionManager.class)) {
            mock.when(ConnectionManager::getConnection)
                    .thenThrow(new SQLException("Sin conexión"));
 
            RuntimeException ex = assertThrows(RuntimeException.class,
                    DatabaseInitializer::inicializar);
 
            assertTrue(ex.getMessage().contains("Error inicializando la base de datos"));
        }
    }
 
    private boolean existeTabla(Connection conn, String nombreTabla) throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreTabla);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}
