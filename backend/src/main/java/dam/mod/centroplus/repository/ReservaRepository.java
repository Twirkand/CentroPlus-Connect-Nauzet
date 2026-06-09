package dam.mod.centroplus.repository;

import dam.mod.centroplus.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Integer> {
    List<ReservaEntity> findByIdUsuario(int idUsuario);
    List<ReservaEntity> findByIdActividad(int idActividad);
    List<ReservaEntity> findByEstado(String estado);
    List<ReservaEntity> findByIdUsuarioAndEstado(int idUsuario, String estado);
    boolean existsByIdActividadAndIdUsuario(int idActividad, int idUsuario);
    boolean existsByIdActividadAndIdUsuarioAndEstado(int idActividad, int idUsuario, String estado);
}