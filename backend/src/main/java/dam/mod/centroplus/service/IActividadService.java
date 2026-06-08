package dam.mod.centroplus.service;

import dam.mod.centroplus.dto.ActividadDTO;
import java.util.List;

public interface IActividadService {
    List<ActividadDTO> findAll();
    ActividadDTO findById(int id);
    ActividadDTO create(ActividadDTO dto);
    ActividadDTO update(int id, ActividadDTO dto);
    void delete(int id);
    boolean reservarPlaza(int idActividad);
    boolean cancelarPlaza(int idActividad);
}