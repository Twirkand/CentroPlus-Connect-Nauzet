package dam.mod.centroplus.business.impl;

import dam.mod.centroplus.adapters.out.persistence.IncidenciaPersistenceAdapter;
import dam.mod.centroplus.adapters.out.persistence.UsuarioPersistenceAdapter;
import dam.mod.centroplus.business.IncidenciaServicePort;
import dam.mod.centroplus.domain.model.Incidencia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidenciaServiceImpl implements IncidenciaServicePort {

    private final IncidenciaPersistenceAdapter incidenciaAdapter;
    private final UsuarioPersistenceAdapter usuarioAdapter;

    @Override
    public List<Incidencia> findAll() {
        return incidenciaAdapter.findAll();
    }

    @Override
    public Incidencia findById(int id) {
        return incidenciaAdapter.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada con id: " + id));
    }

    @Override
    public List<Incidencia> findByIdUsuario(int idUsuario) {
        return incidenciaAdapter.findByIdUsuario(idUsuario);
    }

    @Override
    public List<Incidencia> findByEstado(String estado) {
        validarEstado(estado);
        return incidenciaAdapter.findByEstado(estado);
    }

    @Override
    public List<Incidencia> findByAsunto(String asunto) {
        return incidenciaAdapter.findByAsuntoContainingIgnoreCase(asunto);
    }

    @Override
    public Incidencia create(Incidencia incidencia) {
        validar(incidencia);
        usuarioAdapter.findById(incidencia.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        incidencia.setId(0);
        incidencia.setFecha(LocalDate.now().toString());
        incidencia.setEstado("ABIERTA");
        return incidenciaAdapter.save(incidencia);
    }

    @Override
    public Incidencia update(int id, Incidencia incidencia) {
        Incidencia existing = findById(id);
        validar(incidencia);
        existing.setAsunto(incidencia.getAsunto());
        existing.setDescripcion(incidencia.getDescripcion());
        existing.setEstado(incidencia.getEstado());
        return incidenciaAdapter.save(existing);
    }

    @Override
    public void delete(int id) {
        if (!incidenciaAdapter.existsById(id)) {
            throw new RuntimeException("Incidencia no encontrada con id: " + id);
        }
        incidenciaAdapter.deleteById(id);
    }

    @Override
    public boolean cambiarEstado(int idIncidencia, String nuevoEstado) {
        Incidencia incidencia = findById(idIncidencia);
        validarEstado(nuevoEstado);
        incidencia.setEstado(nuevoEstado);
        incidenciaAdapter.save(incidencia);
        return true;
    }

    private void validar(Incidencia i) {
        if (i == null) throw new IllegalArgumentException("La incidencia no puede ser null");
        if (i.getAsunto() == null || i.getAsunto().isBlank())
            throw new IllegalArgumentException("El asunto es obligatorio");
        if (i.getDescripcion() == null || i.getDescripcion().isBlank())
            throw new IllegalArgumentException("La descripción es obligatoria");
        if (i.getEstado() != null) validarEstado(i.getEstado());
    }

    private void validarEstado(String estado) {
        if (estado == null || (!estado.equals("ABIERTA") &&
                !estado.equals("EN_PROCESO") && !estado.equals("CERRADA"))) {
            throw new IllegalArgumentException("Estado inválido. Valores: ABIERTA, EN_PROCESO, CERRADA");
        }
    }
}
