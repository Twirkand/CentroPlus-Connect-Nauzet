package dam.mod.centroplus.service.impl;

import dam.mod.centroplus.dto.ActividadDTO;
import dam.mod.centroplus.entity.ActividadEntity;
import dam.mod.centroplus.repository.ActividadRepository;
import dam.mod.centroplus.service.IActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActividadServiceImpl implements IActividadService {

    private final ActividadRepository repository;

    @Override
    public List<ActividadDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActividadDTO findById(int id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + id));
    }

    @Override
    public ActividadDTO create(ActividadDTO dto) {
        validar(dto);
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public ActividadDTO update(int id, ActividadDTO dto) {
        ActividadEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + id));
        validar(dto);
        existing.setNombre(dto.getNombre());
        existing.setTipoActividad(dto.getTipoActividad());
        existing.setDuracion(dto.getDuracion());
        existing.setPrecio(dto.getPrecio());
        existing.setPlazasMaximas(dto.getPlazasMaximas());
        existing.setPlazasOcupadas(dto.getPlazasOcupadas());
        return toDTO(repository.save(existing));
    }

    @Override
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Actividad no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean reservarPlaza(int idActividad) {
        ActividadEntity actividad = repository.findById(idActividad)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + idActividad));

        if (actividad.getPlazasOcupadas() >= actividad.getPlazasMaximas()) {
            throw new IllegalArgumentException("No hay plazas disponibles en la actividad");
        }

        actividad.setPlazasOcupadas(actividad.getPlazasOcupadas() + 1);
        repository.save(actividad);
        return true;
    }

    @Override
    public boolean cancelarPlaza(int idActividad) {
        ActividadEntity actividad = repository.findById(idActividad)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + idActividad));

        if (actividad.getPlazasOcupadas() <= 0) {
            throw new IllegalArgumentException("La actividad no tiene plazas ocupadas");
        }

        actividad.setPlazasOcupadas(actividad.getPlazasOcupadas() - 1);
        repository.save(actividad);
        return true;
    }

    private ActividadDTO toDTO(ActividadEntity e) {
        return new ActividadDTO(
                e.getId(),
                e.getNombre(),
                e.getTipoActividad(),
                e.getDuracion(),
                e.getPrecio(),
                e.getPlazasMaximas(),
                e.getPlazasOcupadas()
        );
    }

    private ActividadEntity toEntity(ActividadDTO d) {
        ActividadEntity e = new ActividadEntity();
        e.setNombre(d.getNombre());
        e.setTipoActividad(d.getTipoActividad());
        e.setDuracion(d.getDuracion());
        e.setPrecio(d.getPrecio());
        e.setPlazasMaximas(d.getPlazasMaximas());
        e.setPlazasOcupadas(d.getPlazasOcupadas());
        return e;
    }

    private void validar(ActividadDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("La actividad no puede ser null");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la actividad es obligatorio");
        }
        if (!dto.getTipoActividad().equals("DEPORTIVA") && !dto.getTipoActividad().equals("ACADEMICA")) {
            throw new IllegalArgumentException("Tipo de actividad inválido. Valores: DEPORTIVA, ACADEMICA");
        }
        if (dto.getDuracion() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0");
        }
        if (dto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (dto.getPlazasMaximas() <= 0) {
            throw new IllegalArgumentException("Las plazas máximas deben ser mayores a 0");
        }
        if (dto.getPlazasOcupadas() < 0 || dto.getPlazasOcupadas() > dto.getPlazasMaximas()) {
            throw new IllegalArgumentException("Plazas ocupadas inválidas");
        }
    }
}