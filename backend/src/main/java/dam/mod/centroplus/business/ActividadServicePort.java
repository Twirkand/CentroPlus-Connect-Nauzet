package dam.mod.centroplus.business;

import dam.mod.centroplus.domain.model.Actividad;
import java.util.List;

/**
 * Puerto de entrada (driving port).
 * El controlador depende de esta interfaz, nunca de la implementación.
 */
public interface ActividadServicePort {
    List<Actividad> findAll();
    Actividad findById(int id);
    List<Actividad> findByTipo(String tipo);
    List<Actividad> findByNombre(String nombre);
    List<Actividad> findByPrecioMaximo(double precio);
    Actividad create(Actividad actividad);
    Actividad update(int id, Actividad actividad);
    void delete(int id);
    boolean reservarPlaza(int idActividad);
    boolean cancelarPlaza(int idActividad);
}
