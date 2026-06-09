package dam.mod.centroplus.adapters.out.persistence;

import dam.mod.centroplus.adapters.mapper.ActividadMapper;
import dam.mod.centroplus.domain.model.Actividad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ActividadPersistenceAdapter {

    private final ActividadJpaRepository repository;
    private final ActividadMapper mapper;

    public List<Actividad> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Actividad> findById(int id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public List<Actividad> findByTipoActividad(String tipo) {
        return repository.findByTipoActividad(tipo).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Actividad> findByNombreContainingIgnoreCase(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Actividad> findByPrecioLessThanEqual(double precio) {
        return repository.findByPrecioLessThanEqual(precio).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    public Actividad save(Actividad actividad) {
        ActividadJpaEntity entity = mapper.toJpaEntity(actividad);
        return mapper.toDomain(repository.save(entity));
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}
