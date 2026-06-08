package dam.mod.centroplus.service;

import dam.mod.centroplus.dto.IncidenciaDTO;
import java.util.List;

public interface IIncidenciaService {
    List<IncidenciaDTO> findAll();
    IncidenciaDTO findById(int id);
    IncidenciaDTO create(IncidenciaDTO dto);
    IncidenciaDTO update(int id, IncidenciaDTO dto);
    void delete(int id);
    List<IncidenciaDTO> findByIdUsuario(int idUsuario);
    boolean cambiarEstado(int idIncidencia, String nuevoEstado);
}