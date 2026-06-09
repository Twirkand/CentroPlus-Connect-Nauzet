package dam.mod.centroplus.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, Integer> {
    Optional<UsuarioJpaEntity> findByDni(String dni);
    Optional<UsuarioJpaEntity> findByEmail(String email);
    List<UsuarioJpaEntity> findByTipoUsuario(String tipoUsuario);
    List<UsuarioJpaEntity> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
}
