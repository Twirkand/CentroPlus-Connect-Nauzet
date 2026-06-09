package dam.mod.centroplus.service;

import dam.mod.centroplus.dto.IncidenciaDTO;
import java.util.List;

public interface IIncidenciaService {
    List<IncidenciaDTO> findAll();
    IncidenciaDTO findById(int id);
    List<IncidenciaDTO> findByIdUsuario(int idUsuario);
    List<IncidenciaDTO> findByEstado(String estado);
    List<IncidenciaDTO> findByAsunto(String asunto);
    IncidenciaDTO create(IncidenciaDTO dto);
    IncidenciaDTO update(int id, IncidenciaDTO dto);
    void delete(int id);
    boolean cambiarEstado(int idIncidencia, String nuevoEstado);
}