package dam.mod.centroplus.adapters.out.persistence;

import dam.mod.centroplus.adapters.mapper.IncidenciaMapper;
import dam.mod.centroplus.domain.model.Incidencia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class IncidenciaPersistenceAdapter {

    private final IncidenciaJpaRepository repository;
    private final IncidenciaMapper mapper;

    public List<Incidencia> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public Optional<Incidencia> findById(int id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public List<Incidencia> findByIdUsuario(int idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public List<Incidencia> findByEstado(String estado) {
        return repository.findByEstado(estado).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public List<Incidencia> findByAsuntoContainingIgnoreCase(String asunto) {
        return repository.findByAsuntoContainingIgnoreCase(asunto).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public Incidencia save(Incidencia incidencia) {
        return mapper.toDomain(repository.save(mapper.toJpaEntity(incidencia)));
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}
