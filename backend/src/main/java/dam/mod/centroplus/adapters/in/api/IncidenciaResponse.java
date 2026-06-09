package dam.mod.centroplus.adapters.in.api;

import lombok.Data;

@Data
public class IncidenciaResponse {
    private int id;
    private int idUsuario;
    private String asunto;
    private String descripcion;
    private String fecha;
    private String estado;
}
