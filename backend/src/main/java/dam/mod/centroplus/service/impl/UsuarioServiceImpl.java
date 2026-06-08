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
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public UsuarioDTO create(UsuarioDTO dto, String password) {
        UsuarioEntity entity = toEntity(dto);
        entity.setPassword(passwordEncoder.encode(password));
        return toDTO(repository.save(entity));
    }

    @Override
    public UsuarioDTO update(int id, UsuarioDTO dto) {
        UsuarioEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existing.setNombre(dto.getNombre());
        existing.setEmail(dto.getEmail());
        existing.setTelefono(dto.getTelefono());
        existing.setTipoUsuario(dto.getTipoUsuario());
        return toDTO(repository.save(existing));
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    private UsuarioDTO toDTO(UsuarioEntity e) {
        return new UsuarioDTO(e.getId(), e.getNombre(), e.getDni(),
                e.getEmail(), e.getTelefono(), e.getTipoUsuario());
    }

    private UsuarioEntity toEntity(UsuarioDTO d) {
        UsuarioEntity e = new UsuarioEntity();
        e.setNombre(d.getNombre());
        e.setDni(d.getDni());
        e.setEmail(d.getEmail());
        e.setTelefono(d.getTelefono());
        e.setTipoUsuario(d.getTipoUsuario());
        return e;
    }
}