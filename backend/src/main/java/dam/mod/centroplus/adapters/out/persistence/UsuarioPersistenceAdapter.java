package dam.mod.centroplus.adapters.out.persistence;

import dam.mod.centroplus.adapters.mapper.UsuarioMapper;
import dam.mod.centroplus.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter {

    private final UsuarioJpaRepository repository;
    private final UsuarioMapper mapper;

    public List<Usuario> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public Optional<Usuario> findById(int id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public Optional<UsuarioJpaEntity> findEntityById(int id) {
        return repository.findById(id);
    }

    public Optional<UsuarioJpaEntity> findEntityByDni(String dni) {
        return repository.findByDni(dni);
    }

    public Optional<Usuario> findByDni(String dni) {
        return repository.findByDni(dni).map(mapper::toDomain);
    }

    public Optional<Usuario> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    public List<Usuario> findByTipoUsuario(String tipo) {
        return repository.findByTipoUsuario(tipo).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public List<Usuario> findByNombreContainingIgnoreCase(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public boolean existsByDni(String dni) {
        return repository.existsByDni(dni);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public UsuarioJpaEntity saveEntity(UsuarioJpaEntity entity) {
        return repository.save(entity);
    }

    public Usuario save(Usuario usuario) {
        return mapper.toDomain(repository.save(mapper.toJpaEntity(usuario)));
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}
