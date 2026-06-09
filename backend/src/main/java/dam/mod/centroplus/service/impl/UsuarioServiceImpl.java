package dam.mod.centroplus.service.impl;

import dam.mod.centroplus.dto.UsuarioDTO;
import dam.mod.centroplus.entity.UsuarioEntity;
import dam.mod.centroplus.repository.UsuarioRepository;
import dam.mod.centroplus.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO findById(int id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @Override
    public UsuarioDTO findByDni(String dni) {
        return repository.findByDni(dni)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con DNI: " + dni));
    }

    @Override
    public UsuarioDTO findByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    public List<UsuarioDTO> findByTipo(String tipo) {
        return repository.findByTipoUsuario(tipo).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioDTO> findByNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO create(UsuarioDTO dto, String password) {
        // Validaciones
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (dto.getDni() == null || dto.getDni().isBlank()) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        if (repository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese DNI");
        }
        if (repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        UsuarioEntity entity = toEntity(dto);
        entity.setId(0);
        entity.setPassword(passwordEncoder.encode(password));
        return toDTO(repository.save(entity));
    }

    @Override
    public UsuarioDTO update(int id, UsuarioDTO dto) {
        UsuarioEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        existing.setNombre(dto.getNombre());
        existing.setEmail(dto.getEmail());
        existing.setTelefono(dto.getTelefono());
        existing.setTipoUsuario(dto.getTipoUsuario());

        return toDTO(repository.save(existing));
    }

    @Override
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }


    private UsuarioDTO toDTO(UsuarioEntity e) {
        return new UsuarioDTO(
                e.getId(),
                e.getNombre(),
                e.getDni(),
                e.getEmail(),
                e.getTelefono(),
                e.getTipoUsuario()
        );
    }

    private UsuarioEntity toEntity(UsuarioDTO d) {
        UsuarioEntity e = new UsuarioEntity();
        e.setNombre(d.getNombre());
        e.setDni(d.getDni().toUpperCase().trim());
        e.setEmail(d.getEmail());
        e.setTelefono(d.getTelefono());
        e.setTipoUsuario(d.getTipoUsuario());
        return e;
    }
}