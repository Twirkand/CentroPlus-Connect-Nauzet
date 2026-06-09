package dam.mod.centroplus.business.impl;

import dam.mod.centroplus.adapters.out.persistence.UsuarioJpaEntity;
import dam.mod.centroplus.adapters.out.persistence.UsuarioPersistenceAdapter;
import dam.mod.centroplus.business.UsuarioServicePort;
import dam.mod.centroplus.adapters.mapper.UsuarioMapper;
import dam.mod.centroplus.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioServicePort {

    private final UsuarioPersistenceAdapter persistenceAdapter;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper mapper;

    @Override
    public List<Usuario> findAll() {
        return persistenceAdapter.findAll();
    }

    @Override
    public Usuario findById(int id) {
        return persistenceAdapter.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @Override
    public Usuario findByDni(String dni) {
        return persistenceAdapter.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con DNI: " + dni));
    }

    @Override
    public Usuario findByEmail(String email) {
        return persistenceAdapter.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    public List<Usuario> findByTipo(String tipo) {
        return persistenceAdapter.findByTipoUsuario(tipo);
    }

    @Override
    public List<Usuario> findByNombre(String nombre) {
        return persistenceAdapter.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Usuario create(Usuario usuario, String password) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (usuario.getDni() == null || usuario.getDni().isBlank())
            throw new IllegalArgumentException("El DNI es obligatorio");
        if (usuario.getEmail() == null || usuario.getEmail().isBlank())
            throw new IllegalArgumentException("El email es obligatorio");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        if (persistenceAdapter.existsByDni(usuario.getDni()))
            throw new IllegalArgumentException("Ya existe un usuario con ese DNI");
        if (persistenceAdapter.existsByEmail(usuario.getEmail()))
            throw new IllegalArgumentException("Ya existe un usuario con ese email");

        UsuarioJpaEntity entity = mapper.toJpaEntity(usuario);
        entity.setId(0);
        entity.setDni(usuario.getDni().toUpperCase().trim());
        entity.setPassword(passwordEncoder.encode(password));
        return mapper.toDomain(persistenceAdapter.saveEntity(entity));
    }

    @Override
    public Usuario update(int id, Usuario usuario) {
        UsuarioJpaEntity existing = persistenceAdapter.findEntityById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        existing.setNombre(usuario.getNombre());
        existing.setEmail(usuario.getEmail());
        existing.setTelefono(usuario.getTelefono());
        existing.setTipoUsuario(usuario.getTipoUsuario());
        return mapper.toDomain(persistenceAdapter.saveEntity(existing));
    }

    @Override
    public void delete(int id) {
        if (!persistenceAdapter.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        persistenceAdapter.deleteById(id);
    }
}
