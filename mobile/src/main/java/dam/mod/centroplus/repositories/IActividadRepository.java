package dam.mod.centroplus.repositories;

import java.util.List;

import dam.mod.centroplus.models.Actividad;

public interface IActividadRepository {

    /**
     * Funcion que encuentra todas las actividades
     * @return Todas las actividades
     */
    List<Actividad> findAll();

    /**
     * Funcion que busca una actividad por su id
     * @param id id de la actividad
     * @return
     */
    Actividad findById(int id);

    /**
     * Funcion que guarda una actividad
     * @param actividad actividad a guardar
     * @return
     */
    boolean save(Actividad actividad);

    /**
     * Funcion que actualiza la actividad
     * @param actividad
     * @return
     */
    boolean update(Actividad actividad);

    /**
     * Funcion que borra una actividad por su id
     * @param id id de la actividad
     * @return
     */
    boolean delete(int id);
}