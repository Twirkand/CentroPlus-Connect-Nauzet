package dam.mod.centroplus.adapters.in.api;

import lombok.Data;

@Data
public class ReservaRequest {
    private int idUsuario;
    private int idActividad;
    private String fecha;
    private String estado;
}
