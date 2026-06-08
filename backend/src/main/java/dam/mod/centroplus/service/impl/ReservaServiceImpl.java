package dam.mod.centroplus.service.impl;

import dam.mod.centroplus.dto.ReservaDTO;
import dam.mod.centroplus.entity.ActividadEntity;
import dam.mod.centroplus.entity.ReservaEntity;
import dam.mod.centroplus.repository.ActividadRepository;
import dam.mod.centroplus.repository.ReservaRepository;
import dam.mod.centroplus.repository.UsuarioRepository;
import dam.mod.centroplus.service.IActividadService;
import dam.mod.centroplus.service.IReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements IReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ActividadRepository actividadRepository;
    private final IActividadService actividadService;

    @Override
    public List<ReservaDTO> findAll() {
        return reservaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReservaDTO findById(int id) {
        return reservaRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    @Override
    public ReservaDTO create(ReservaDTO dto) {
        usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        actividadRepository.findById(dto.getIdActividad())
                .orElseThrow(() -> new IllegalArgumentException("La actividad no existe"));

        if (!actividadService.reservarPlaza(dto.getIdActividad())) {
            throw new IllegalArgumentException("No hay plazas disponibles");
        }

        boolean yaReservado = reservaRepository
                .existsByIdActividadAndIdUsuario(dto.getIdActividad(), dto.getIdUsuario());
        if (yaReservado) {
            actividadService.cancelarPlaza(dto.getIdActividad());
            throw new IllegalArgumentException("Ya existe una reserva para esta actividad");
        }

        ReservaEntity entity = toEntity(dto);
        if (entity.getFecha() == null || entity.getFecha().isBlank()) {
            entity.setFecha(LocalDate.now().toString());
        }
        if (entity.getEstado() == null || entity.getEstado().isBlank()) {
            entity.setEstado("ACTIVA");
        }

        return toDTO(reservaRepository.save(entity));
    }

    @Override
    public ReservaDTO update(int id, ReservaDTO dto) {
        ReservaEntity existing = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
        validarEstado(dto.getEstado());
        existing.setFecha(dto.getFecha());
        existing.setEstado(dto.getEstado());
        return toDTO(reservaRepository.save(existing));
    }

    @Override
    public void delete(int id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada con id: " + id);
        }
        reservaRepository.deleteById(id);
    }

    @Override
    public List<ReservaDTO> findByIdUsuario(int idUsuario) {
        return reservaRepository.findByIdUsuario(idUsuario).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean cambiarEstado(int idReserva, String nuevoEstado) {
        ReservaEntity reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + idReserva));

        validarEstado(nuevoEstado);

        if ("CANCELADA".equals(nuevoEstado) && !"CANCELADA".equals(reserva.getEstado())) {
            actividadService.cancelarPlaza(reserva.getIdActividad());
        }

        if ("ACTIVA".equals(nuevoEstado) && "CANCELADA".equals(reserva.getEstado())) {
            actividadService.reservarPlaza(reserva.getIdActividad());
        }

        reserva.setEstado(nuevoEstado);
        reservaRepository.save(reserva);
        return true;
    }

    @Override
    public boolean cancelarReserva(int idReserva, int idUsuario) {
        ReservaEntity reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + idReserva));

        if (reserva.getIdUsuario() != idUsuario) {
            throw new IllegalArgumentException("La reserva no pertenece a este usuario");
        }

        actividadService.cancelarPlaza(reserva.getIdActividad());
        reservaRepository.deleteById(idReserva);
        return true;
    }

    private ReservaDTO toDTO(ReservaEntity e) {
        String nombreActividad = actividadRepository.findById(e.getIdActividad())
                .map(ActividadEntity::getNombre)
                .orElse("");

        return new ReservaDTO(
                e.getId(),
                e.getIdUsuario(),
                e.getIdActividad(),
                e.getFecha(),
                e.getEstado(),
                nombreActividad);
    }

    private ReservaEntity toEntity(ReservaDTO d) {
        ReservaEntity e = new ReservaEntity();
        e.setIdUsuario(d.getIdUsuario());
        e.setIdActividad(d.getIdActividad());
        e.setFecha(d.getFecha());
        e.setEstado(d.getEstado());
        return e;
    }

    private void validarEstado(String estado) {
        if (estado == null ||
                (!estado.equals("ACTIVA") &&
                        !estado.equals("CANCELADA") &&
                        !estado.equals("COMPLETADA"))) {
            throw new IllegalArgumentException(
                    "Estado inválido. Valores permitidos: ACTIVA, CANCELADA, COMPLETADA");
        }
    }
}