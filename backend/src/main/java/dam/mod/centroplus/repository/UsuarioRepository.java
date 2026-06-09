package dam.mod.centroplus.repository;

import dam.mod.centroplus.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    Optional<UsuarioEntity> findByDni(String dni);
    Optional<UsuarioEntity> findByEmail(String email);
    List<UsuarioEntity> findByTipoUsuario(String tipoUsuario);
    List<UsuarioEntity> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
}