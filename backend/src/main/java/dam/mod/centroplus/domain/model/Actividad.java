package dam.mod.centroplus.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actividad {
    private int id;
    private String nombre;
    private String tipoActividad;
    private int duracion;
    private double precio;
    private int plazasMaximas;
    private int plazasOcupadas;
}
