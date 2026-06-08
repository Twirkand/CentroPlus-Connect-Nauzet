package dam.mod.centroplus.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadDTO {
    private int id;
    private String nombre;
    private String tipoActividad;
    private int duracion;
    private double precio;
    private int plazasMaximas;
    private int plazasOcupadas;
}