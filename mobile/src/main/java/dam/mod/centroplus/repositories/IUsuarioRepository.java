package dam.mod.centroplus.repositories;

import java.util.List;

import dam.mod.centroplus.models.Usuario;

public interface IUsuarioRepository {
    List<Usuario> findAll();
    Usuario findById(int id);
    boolean save(Usuario usuario);
    boolean update(Usuario usuario);
    boolean delete(int id);
    Usuario login(String dni, String password);
    Usuario findByDni(String dni);
}