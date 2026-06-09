package dam.mod.centroplus.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaJpaRepository extends JpaRepository<ReservaJpaEntity, Integer> {
    List<ReservaJpaEntity> findByIdUsuario(int idUsuario);
    List<ReservaJpaEntity> findByIdActividad(int idActividad);
    List<ReservaJpaEntity> findByEstado(String estado);
    List<ReservaJpaEntity> findByIdUsuarioAndEstado(int idUsuario, String estado);
    boolean existsByIdActividadAndIdUsuario(int idActividad, int idUsuario);
    boolean existsByIdActividadAndIdUsuarioAndEstado(int idActividad, int idUsuario, String estado);
}
