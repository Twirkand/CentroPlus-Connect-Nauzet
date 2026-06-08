package dam.mod.centroplus.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tipoUsuario;
    private String nombre;
}