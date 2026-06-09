package dam.mod.centroplus.business;

import dam.mod.centroplus.domain.model.Reserva;
import java.util.List;

public interface ReservaServicePort {
    List<Reserva> findAll();
    Reserva findById(int id);
    List<Reserva> findByIdUsuario(int idUsuario);
    List<Reserva> findByIdActividad(int idActividad);
    List<Reserva> findByEstado(String estado);
    Reserva create(Reserva reserva);
    Reserva update(int id, Reserva reserva);
    void delete(int id);
    boolean cambiarEstado(int idReserva, String nuevoEstado);
    boolean cancelarReserva(int idReserva, int idUsuario);
}
