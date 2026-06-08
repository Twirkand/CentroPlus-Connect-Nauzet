package dam.mod.centroplus.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private int id;
    private String nombre;
    private String dni;
    private String email;
    private String telefono;
    private String tipoUsuario;
}