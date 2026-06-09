package dam.mod.centroplus.repository;

import dam.mod.centroplus.entity.IncidenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidenciaRepository extends JpaRepository<IncidenciaEntity, Integer> {
    List<IncidenciaEntity> findByIdUsuario(int idUsuario);
    List<IncidenciaEntity> findByEstado(String estado);
    List<IncidenciaEntity> findByIdUsuarioAndEstado(int idUsuario, String estado);
    List<IncidenciaEntity> findByAsuntoContainingIgnoreCase(String asunto);
}