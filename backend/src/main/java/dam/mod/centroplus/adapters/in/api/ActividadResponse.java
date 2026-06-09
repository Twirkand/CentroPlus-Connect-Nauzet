package dam.mod.centroplus.adapters.in.api;

import lombok.Data;

/** Lo que el servidor devuelve al cliente. */
@Data
public class ActividadResponse {
    private int id;
    private String nombre;
    private String tipoActividad;
    private int duracion;
    private double precio;
    private int plazasMaximas;
    private int plazasOcupadas;
}
