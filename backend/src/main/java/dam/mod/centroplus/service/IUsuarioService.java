package dam.mod.centroplus.service;

import dam.mod.centroplus.dto.UsuarioDTO;
import java.util.List;

public interface IUsuarioService {
    List<UsuarioDTO> findAll();
    UsuarioDTO findById(int id);
    UsuarioDTO create(UsuarioDTO dto, String password);
    UsuarioDTO update(int id, UsuarioDTO dto);
    void delete(int id);
}