package dam.mod.centroplus.service;

import dam.mod.centroplus.dto.ReservaDTO;
import java.util.List;

public interface IReservaService {
    List<ReservaDTO> findAll();
    ReservaDTO findById(int id);
    List<ReservaDTO> findByIdUsuario(int idUsuario);
    List<ReservaDTO> findByIdActividad(int idActividad);
    List<ReservaDTO> findByEstado(String estado);
    ReservaDTO create(ReservaDTO dto);
    ReservaDTO update(int id, ReservaDTO dto);
    void delete(int id);
    boolean cambiarEstado(int idReserva, String nuevoEstado);
    boolean cancelarReserva(int idReserva, int idUsuario);
}