package dam.mod.centroplus.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidenciaJpaRepository extends JpaRepository<IncidenciaJpaEntity, Integer> {
    List<IncidenciaJpaEntity> findByIdUsuario(int idUsuario);
    List<IncidenciaJpaEntity> findByEstado(String estado);
    List<IncidenciaJpaEntity> findByIdUsuarioAndEstado(int idUsuario, String estado);
    List<IncidenciaJpaEntity> findByAsuntoContainingIgnoreCase(String asunto);
}
