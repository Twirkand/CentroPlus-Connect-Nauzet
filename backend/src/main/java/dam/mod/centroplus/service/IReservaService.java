package dam.mod.centroplus.service;

import dam.mod.centroplus.dto.ReservaDTO;
import java.util.List;

public interface IReservaService {
    List<ReservaDTO> findAll();
    ReservaDTO findById(int id);
    ReservaDTO create(ReservaDTO dto);
    ReservaDTO update(int id, ReservaDTO dto);
    void delete(int id);
    List<ReservaDTO> findByIdUsuario(int idUsuario);
    boolean cambiarEstado(int idReserva, String nuevoEstado);
    boolean cancelarReserva(int idReserva, int idUsuario);
}