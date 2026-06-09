package dam.mod.centroplus.adapters.out.persistence;

import dam.mod.centroplus.adapters.mapper.ReservaMapper;
import dam.mod.centroplus.domain.model.Reserva;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservaPersistenceAdapter {

    private final ReservaJpaRepository repository;
    private final ReservaMapper mapper;

    public List<Reserva> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public Optional<Reserva> findById(int id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public List<Reserva> findByIdUsuario(int idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public List<Reserva> findByIdActividad(int idActividad) {
        return repository.findByIdActividad(idActividad).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public List<Reserva> findByEstado(String estado) {
        return repository.findByEstado(estado).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public boolean existsByIdActividadAndIdUsuarioAndEstado(int idActividad, int idUsuario, String estado) {
        return repository.existsByIdActividadAndIdUsuarioAndEstado(idActividad, idUsuario, estado);
    }

    public Reserva save(Reserva reserva) {
        return mapper.toDomain(repository.save(mapper.toJpaEntity(reserva)));
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}
