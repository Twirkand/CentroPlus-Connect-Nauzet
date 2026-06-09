package dam.mod.centroplus.business.impl;

import dam.mod.centroplus.adapters.out.persistence.ActividadPersistenceAdapter;
import dam.mod.centroplus.business.ActividadServicePort;
import dam.mod.centroplus.domain.model.Actividad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActividadServiceImpl implements ActividadServicePort {

    private final ActividadPersistenceAdapter persistenceAdapter;

    @Override
    public List<Actividad> findAll() {
        return persistenceAdapter.findAll();
    }

    @Override
    public Actividad findById(int id) {
        return persistenceAdapter.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + id));
    }

    @Override
    public List<Actividad> findByTipo(String tipo) {
        if (tipo == null || (!tipo.equals("DEPORTIVA") && !tipo.equals("ACADEMICA"))) {
            throw new IllegalArgumentException("Tipo inválido. Valores: DEPORTIVA, ACADEMICA");
        }
        return persistenceAdapter.findByTipoActividad(tipo);
    }

    @Override
    public List<Actividad> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        return persistenceAdapter.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Actividad> findByPrecioMaximo(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        return persistenceAdapter.findByPrecioLessThanEqual(precio);
    }

    @Override
    public Actividad create(Actividad actividad) {
        validar(actividad);
        actividad.setPlazasOcupadas(0);
        return persistenceAdapter.save(actividad);
    }

    @Override
    public Actividad update(int id, Actividad actividad) {
        Actividad existing = findById(id);
        validar(actividad);
        actividad.setId(existing.getId());
        return persistenceAdapter.save(actividad);
    }

    @Override
    public void delete(int id) {
        if (!persistenceAdapter.existsById(id)) {
            throw new RuntimeException("Actividad no encontrada con id: " + id);
        }
        persistenceAdapter.deleteById(id);
    }

    @Override
    public boolean reservarPlaza(int idActividad) {
        Actividad actividad = findById(idActividad);
        if (actividad.getPlazasOcupadas() >= actividad.getPlazasMaximas()) {
            throw new IllegalArgumentException("No hay plazas disponibles en la actividad");
        }
        actividad.setPlazasOcupadas(actividad.getPlazasOcupadas() + 1);
        persistenceAdapter.save(actividad);
        return true;
    }

    @Override
    public boolean cancelarPlaza(int idActividad) {
        Actividad actividad = findById(idActividad);
        if (actividad.getPlazasOcupadas() <= 0) {
            throw new IllegalArgumentException("La actividad no tiene plazas ocupadas");
        }
        actividad.setPlazasOcupadas(actividad.getPlazasOcupadas() - 1);
        persistenceAdapter.save(actividad);
        return true;
    }

    private void validar(Actividad a) {
        if (a == null) throw new IllegalArgumentException("La actividad no puede ser null");
        if (a.getNombre() == null || a.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre de la actividad es obligatorio");
        if (a.getTipoActividad() == null ||
                (!a.getTipoActividad().equals("DEPORTIVA") && !a.getTipoActividad().equals("ACADEMICA")))
            throw new IllegalArgumentException("Tipo inválido. Valores: DEPORTIVA, ACADEMICA");
        if (a.getDuracion() <= 0)
            throw new IllegalArgumentException("La duración debe ser mayor a 0");
        if (a.getPrecio() < 0)
            throw new IllegalArgumentException("El precio no puede ser negativo");
        if (a.getPlazasMaximas() <= 0)
            throw new IllegalArgumentException("Las plazas máximas deben ser mayores a 0");
        if (a.getPlazasOcupadas() < 0 || a.getPlazasOcupadas() > a.getPlazasMaximas())
            throw new IllegalArgumentException("Plazas ocupadas inválidas");
    }
}
