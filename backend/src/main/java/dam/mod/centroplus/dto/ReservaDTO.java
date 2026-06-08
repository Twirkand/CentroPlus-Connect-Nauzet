package dam.mod.centroplus.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private int id;
    private int idUsuario;
    private int idActividad;
    private String fecha;
    private String estado;
    private String nombreActividad;
}