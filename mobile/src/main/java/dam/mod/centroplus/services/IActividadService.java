package dam.mod.centroplus.services;

import java.util.List;

import dam.mod.centroplus.models.Actividad;

/**
 * Interfaz que define las operaciones de negocio relacionadas con actividades.
 */
public interface IActividadService {

    /**
     * Devuelve todas las actividades disponibles en el sistema.
     *
     * @return lista de actividades
     */
    List<Actividad> findAll();

    /**
     * Busca una actividad por su identificador.
     *
     * @param id ID de la actividad
     * @return actividad encontrada o null si no existe
     */
    Actividad findById(int id);

    /**
     * Crea una nueva actividad en el sistema.
     *
     * @param actividad objeto actividad a crear
     * @return true si la creación fue correcta, false en caso contrario
     */
    boolean create(Actividad actividad);

    /**
     * Actualiza una actividad existente.
     *
     * @param actividad objeto actividad con datos actualizados
     * @return true si la actualización fue correcta, false en caso contrario
     */
    boolean update(Actividad actividad);

    /**
     * Elimina una actividad por su identificador.
     *
     * @param id ID de la actividad
     * @return true si la eliminación fue correcta, false en caso contrario
     */
    boolean delete(int id);

    /**
     * Reserva una plaza en una actividad.
     *
     * @param idActividad ID de la actividad
     * @return true si la reserva fue correcta, false en caso contrario
     */
    boolean reservarPlaza(int idActividad);

    /**
     * Cancela una plaza reservada en una actividad.
     *
     * @param idActividad ID de la actividad
     * @return true si la cancelación fue correcta, false en caso contrario
     */
    boolean cancelarPlaza(int idActividad);

    /**
     * Devuelve todas las actividades que están completas (sin plazas disponibles).
     *
     * @return lista de actividades completas
     */
    List<Actividad> findCompletas();

    /**
     * Calcula el número de plazas disponibles para una actividad.
     *
     * @param idActividad ID de la actividad
     * @return número de plazas libres
     */
    int calcularPlazasDisponibles(int idActividad);

    /**
     * Calcula los ingresos totales generados por todas las actividades.
     *
     * @return ingresos totales del sistema
     */
    double calcularIngresosTotales();
}