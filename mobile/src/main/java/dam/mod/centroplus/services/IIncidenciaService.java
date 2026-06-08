package dam.mod.centroplus.services;

import java.util.List;

import dam.mod.centroplus.models.Incidencia;

/**
 * Interfaz que define las operaciones de negocio relacionadas con incidencias.
 */
public interface IIncidenciaService {

    /**
     * Devuelve todas las incidencias registradas en el sistema.
     *
     * @return lista de incidencias
     */
    List<Incidencia> findAll();

    /**
     * Busca una incidencia por su identificador.
     *
     * @param id ID de la incidencia
     * @return incidencia encontrada o null si no existe
     */
    Incidencia findById(int id);

    /**
     * Crea una nueva incidencia en el sistema.
     *
     * @param incidencia objeto incidencia a crear
     * @return true si la creación fue correcta, false en caso contrario
     */
    boolean create(Incidencia incidencia);

    /**
     * Actualiza una incidencia existente.
     *
     * @param incidencia objeto incidencia con datos actualizados
     * @return true si la actualización fue correcta, false en caso contrario
     */
    boolean update(Incidencia incidencia);

    /**
     * Elimina una incidencia por su identificador.
     *
     * @param id ID de la incidencia
     * @return true si la eliminación fue correcta, false en caso contrario
     */
    boolean delete(int id);

    /**
     * Cambia el estado de una incidencia.
     *
     * @param idIncidencia ID de la incidencia
     * @param nuevoEstado nuevo estado (ej: ABIERTA, EN_PROCESO, CERRADA)
     * @return true si el cambio fue exitoso, false en caso contrario
     */
    boolean cambiarEstado(int idIncidencia, String nuevoEstado);

    /**
     * Obtiene todas las incidencias asociadas a un usuario.
     *
     * @param idUsuario ID del usuario
     * @return lista de incidencias del usuario
     */
    List<Incidencia> findByUsuario(int idUsuario);
}