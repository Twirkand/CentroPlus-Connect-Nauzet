package dam.mod.centroplus.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String dni;
    private String password;
}