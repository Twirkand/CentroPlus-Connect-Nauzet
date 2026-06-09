package dam.mod.centroplus.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    private int id;
    private int idUsuario;
    private int idActividad;
    private String fecha;
    private String estado;
    private String nombreActividad;
}
