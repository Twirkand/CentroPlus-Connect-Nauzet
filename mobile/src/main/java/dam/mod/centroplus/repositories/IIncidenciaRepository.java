package dam.mod.centroplus.repositories;

import java.util.List;

import dam.mod.centroplus.models.Incidencia;

public interface IIncidenciaRepository {
    List<Incidencia> findAll();
    Incidencia findById(int id);
    boolean save(Incidencia incidencia);
    boolean update(Incidencia incidencia);
    boolean delete(int id);
}