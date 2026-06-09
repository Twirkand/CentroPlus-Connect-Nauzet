package dam.mod.centroplus.repository;

import dam.mod.centroplus.entity.ActividadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActividadRepository extends JpaRepository<ActividadEntity, Integer> {
    List<ActividadEntity> findByTipoActividad(String tipoActividad);
    List<ActividadEntity> findByNombreContainingIgnoreCase(String nombre);
    List<ActividadEntity> findByPrecioLessThanEqual(double precio);
}