package dam.mod.centroplus.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidenciaDTO {
    private int id;
    private int idUsuario;
    private String asunto;
    private String descripcion;
    private String fecha;
    private String estado;
}