package dam.mod.centroplus.repository;

import dam.mod.centroplus.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Integer> {
    List<ReservaEntity> findByIdUsuario(int idUsuario);
    boolean existsByIdActividadAndIdUsuario(int idActividad, int idUsuario);
}