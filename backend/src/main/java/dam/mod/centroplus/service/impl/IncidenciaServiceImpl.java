package dam.mod.centroplus.service.impl;

import dam.mod.centroplus.dto.IncidenciaDTO;
import dam.mod.centroplus.entity.IncidenciaEntity;
import dam.mod.centroplus.repository.IncidenciaRepository;
import dam.mod.centroplus.repository.UsuarioRepository;
import dam.mod.centroplus.service.IIncidenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidenciaServiceImpl implements IIncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<IncidenciaDTO> findAll() {
        return incidenciaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IncidenciaDTO findById(int id) {
        return incidenciaRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada con id: " + id));
    }

    @Override
    public List<IncidenciaDTO> findByIdUsuario(int idUsuario) {
        return incidenciaRepository.findByIdUsuario(idUsuario).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncidenciaDTO> findByEstado(String estado) {
        validarEstado(estado);
        return incidenciaRepository.findByEstado(estado).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncidenciaDTO> findByAsunto(String asunto) {
        return incidenciaRepository.findByAsuntoContainingIgnoreCase(asunto).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IncidenciaDTO create(IncidenciaDTO dto) {
        validar(dto);

        usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        IncidenciaEntity entity = toEntity(dto);
        entity.setId(0);
        entity.setFecha(LocalDate.now().toString());
        entity.setEstado("ABIERTA");

        return toDTO(incidenciaRepository.save(entity));
    }

    @Override
    public IncidenciaDTO update(int id, IncidenciaDTO dto) {
        IncidenciaEntity existing = incidenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada con id: " + id));
        validar(dto);
        existing.setAsunto(dto.getAsunto());
        existing.setDescripcion(dto.getDescripcion());
        existing.setEstado(dto.getEstado());
        return toDTO(incidenciaRepository.save(existing));
    }

    @Override
    public void delete(int id) {
        if (!incidenciaRepository.existsById(id)) {
            throw new RuntimeException("Incidencia no encontrada con id: " + id);
        }
        incidenciaRepository.deleteById(id);
    }

    @Override
    public boolean cambiarEstado(int idIncidencia, String nuevoEstado) {
        IncidenciaEntity incidencia = incidenciaRepository.findById(idIncidencia)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada con id: " + idIncidencia));
        validarEstado(nuevoEstado);
        incidencia.setEstado(nuevoEstado);
        incidenciaRepository.save(incidencia);
        return true;
    }


    private IncidenciaDTO toDTO(IncidenciaEntity e) {
        return new IncidenciaDTO(e.getId(), e.getIdUsuario(), e.getAsunto(),
                e.getDescripcion(), e.getFecha(), e.getEstado());
    }

    private IncidenciaEntity toEntity(IncidenciaDTO d) {
        IncidenciaEntity e = new IncidenciaEntity();
        e.setIdUsuario(d.getIdUsuario()); 
        e.setAsunto(d.getAsunto());
        e.setDescripcion(d.getDescripcion());
        e.setFecha(d.getFecha());
        e.setEstado(d.getEstado());
        return e;
    }


    private void validar(IncidenciaDTO dto) {
        if (dto == null) throw new IllegalArgumentException("La incidencia no puede ser null");
        if (dto.getAsunto() == null || dto.getAsunto().isBlank())
            throw new IllegalArgumentException("El asunto es obligatorio");
        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank())
            throw new IllegalArgumentException("La descripción es obligatoria");
        if (dto.getEstado() != null) validarEstado(dto.getEstado());
    }

    private void validarEstado(String estado) {
        if (estado == null || (!estado.equals("ABIERTA") &&
                !estado.equals("EN_PROCESO") && !estado.equals("CERRADA"))) {
            throw new IllegalArgumentException(
                    "Estado inválido. Valores: ABIERTA, EN_PROCESO, CERRADA");
        }
    }
}