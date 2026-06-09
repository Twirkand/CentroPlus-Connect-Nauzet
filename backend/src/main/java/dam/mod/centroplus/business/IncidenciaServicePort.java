package dam.mod.centroplus.business;

import dam.mod.centroplus.domain.model.Incidencia;
import java.util.List;

public interface IncidenciaServicePort {
    List<Incidencia> findAll();
    Incidencia findById(int id);
    List<Incidencia> findByIdUsuario(int idUsuario);
    List<Incidencia> findByEstado(String estado);
    List<Incidencia> findByAsunto(String asunto);
    Incidencia create(Incidencia incidencia);
    Incidencia update(int id, Incidencia incidencia);
    void delete(int id);
    boolean cambiarEstado(int idIncidencia, String nuevoEstado);
}
