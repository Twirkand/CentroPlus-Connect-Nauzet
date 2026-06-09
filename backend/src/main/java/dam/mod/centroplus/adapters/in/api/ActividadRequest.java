package dam.mod.centroplus.adapters.in.api;

import lombok.Data;

/** Lo que el cliente envía al crear/actualizar una actividad. */
@Data
public class ActividadRequest {
    private String nombre;
    private String tipoActividad;
    private int duracion;
    private double precio;
    private int plazasMaximas;
    private int plazasOcupadas;
}
