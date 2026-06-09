package dam.mod.centroplus.adapters.in.api;

import lombok.Data;

@Data
public class IncidenciaRequest {
    private int idUsuario;
    private String asunto;
    private String descripcion;
    private String estado;
}
