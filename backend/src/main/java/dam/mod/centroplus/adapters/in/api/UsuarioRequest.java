package dam.mod.centroplus.adapters.in.api;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String nombre;
    private String dni;
    private String email;
    private String telefono;
    private String tipoUsuario;
}
