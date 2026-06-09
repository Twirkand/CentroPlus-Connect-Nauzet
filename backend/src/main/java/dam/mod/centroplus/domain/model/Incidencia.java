package dam.mod.centroplus.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incidencia {
    private int id;
    private int idUsuario;
    private String asunto;
    private String descripcion;
    private String fecha;
    private String estado;
}
