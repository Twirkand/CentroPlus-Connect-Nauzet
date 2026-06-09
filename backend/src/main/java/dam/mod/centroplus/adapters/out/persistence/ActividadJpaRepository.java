package dam.mod.centroplus.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActividadJpaRepository extends JpaRepository<ActividadJpaEntity, Integer> {
    List<ActividadJpaEntity> findByTipoActividad(String tipoActividad);
    List<ActividadJpaEntity> findByNombreContainingIgnoreCase(String nombre);
    List<ActividadJpaEntity> findByPrecioLessThanEqual(double precio);
}
