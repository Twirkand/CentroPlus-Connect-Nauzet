package dam.mod.centroplus.repositories;

import java.util.List;

import dam.mod.centroplus.models.Reserva;

public interface IReservaRepository {
    List<Reserva> findAll();
    Reserva findById(int id);
    boolean save(Reserva reserva);
    boolean update(Reserva reserva);
    boolean delete(int id);
    boolean existsReserva(int actividadId, int usuarioId);
    List<Reserva> findByIdUsuario(int idUsuario);
    boolean cambiarEstado(int idReserva, String nuevoEstado);
}