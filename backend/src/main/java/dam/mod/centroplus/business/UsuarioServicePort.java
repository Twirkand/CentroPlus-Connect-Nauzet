package dam.mod.centroplus.business;

import dam.mod.centroplus.domain.model.Usuario;
import java.util.List;

public interface UsuarioServicePort {
    List<Usuario> findAll();
    Usuario findById(int id);
    Usuario findByDni(String dni);
    Usuario findByEmail(String email);
    List<Usuario> findByTipo(String tipo);
    List<Usuario> findByNombre(String nombre);
    Usuario create(Usuario usuario, String password);
    Usuario update(int id, Usuario usuario);
    void delete(int id);
}
