package dam.mod.centroplus.business.impl;

import dam.mod.centroplus.adapters.out.persistence.ActividadPersistenceAdapter;
import dam.mod.centroplus.adapters.out.persistence.ReservaPersistenceAdapter;
import dam.mod.centroplus.adapters.out.persistence.UsuarioPersistenceAdapter;
import dam.mod.centroplus.business.ActividadServicePort;
import dam.mod.centroplus.business.ReservaServicePort;
import dam.mod.centroplus.domain.model.Reserva;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaServicePort {

    private final ReservaPersistenceAdapter reservaAdapter;
    private final UsuarioPersistenceAdapter usuarioAdapter;
    private final ActividadPersistenceAdapter actividadAdapter;
    private final ActividadServicePort actividadService;

    @Override
    public List<Reserva> findAll() {
        return enrichAll(reservaAdapter.findAll());
    }

    @Override
    public Reserva findById(int id) {
        return enrich(reservaAdapter.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id)));
    }

    @Override
    public List<Reserva> findByIdUsuario(int idUsuario) {
        return enrichAll(reservaAdapter.findByIdUsuario(idUsuario));
    }

    @Override
    public List<Reserva> findByIdActividad(int idActividad) {
        return enrichAll(reservaAdapter.findByIdActividad(idActividad));
    }

    @Override
    public List<Reserva> findByEstado(String estado) {
        validarEstado(estado);
        return enrichAll(reservaAdapter.findByEstado(estado));
    }

    @Override
    public Reserva create(Reserva reserva) {
        usuarioAdapter.findById(reserva.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        actividadAdapter.findById(reserva.getIdActividad())
                .orElseThrow(() -> new IllegalArgumentException("La actividad no existe"));

        boolean yaReservado = reservaAdapter.existsByIdActividadAndIdUsuarioAndEstado(
                reserva.getIdActividad(), reserva.getIdUsuario(), "ACTIVA");
        if (yaReservado) {
            throw new IllegalArgumentException("Ya existe una reserva activa para esta actividad");
        }

        actividadService.reservarPlaza(reserva.getIdActividad());

        reserva.setId(0);
        reserva.setFecha(LocalDate.now().toString());
        reserva.setEstado("ACTIVA");
        return enrich(reservaAdapter.save(reserva));
    }

    @Override
    public Reserva update(int id, Reserva reserva) {
        Reserva existing = findById(id);
        validarEstado(reserva.getEstado());
        existing.setFecha(reserva.getFecha());
        existing.setEstado(reserva.getEstado());
        return enrich(reservaAdapter.save(existing));
    }

    @Override
    public void delete(int id) {
        if (!reservaAdapter.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada con id: " + id);
        }
        reservaAdapter.deleteById(id);
    }

    @Override
    public boolean cambiarEstado(int idReserva, String nuevoEstado) {
        Reserva reserva = findById(idReserva);
        validarEstado(nuevoEstado);
        if ("CANCELADA".equals(nuevoEstado) && !"CANCELADA".equals(reserva.getEstado())) {
            actividadService.cancelarPlaza(reserva.getIdActividad());
        }
        if ("ACTIVA".equals(nuevoEstado) && "CANCELADA".equals(reserva.getEstado())) {
            actividadService.reservarPlaza(reserva.getIdActividad());
        }
        reserva.setEstado(nuevoEstado);
        reservaAdapter.save(reserva);
        return true;
    }

    @Override
    public boolean cancelarReserva(int idReserva, int idUsuario) {
        Reserva reserva = findById(idReserva);
        if (reserva.getIdUsuario() != idUsuario) {
            throw new IllegalArgumentException("La reserva no pertenece a este usuario");
        }
        actividadService.cancelarPlaza(reserva.getIdActividad());
        reservaAdapter.deleteById(idReserva);
        return true;
    }

    /** Enriquece la reserva con el nombre de la actividad. */
    private Reserva enrich(Reserva reserva) {
        actividadAdapter.findById(reserva.getIdActividad())
                .ifPresent(a -> reserva.setNombreActividad(a.getNombre()));
        return reserva;
    }

    private List<Reserva> enrichAll(List<Reserva> reservas) {
        reservas.forEach(this::enrich);
        return reservas;
    }

    private void validarEstado(String estado) {
        if (estado == null || (!estado.equals("ACTIVA") &&
                !estado.equals("CANCELADA") && !estado.equals("COMPLETADA"))) {
            throw new IllegalArgumentException("Estado inválido. Valores: ACTIVA, CANCELADA, COMPLETADA");
        }
    }
}
