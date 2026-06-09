package dam.mod.centroplus.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private int id;
    private String nombre;
    private String dni;
    private String email;
    private String telefono;
    private String tipoUsuario;
}
