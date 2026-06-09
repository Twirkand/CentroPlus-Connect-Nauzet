package dam.mod.centroplus.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidad JPA — vive en el adaptador de salida.
 * El dominio nunca la ve directamente.
 */
@Entity
@Table(name = "actividades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String nombre;
    @Column(name = "tipo_actividad", nullable = false)
    private String tipoActividad;
    @Column(nullable = false)
    private int duracion;
    @Column(nullable = false)
    private double precio;
    @Column(name = "plazas_maximas", nullable = false)
    private int plazasMaximas;
    @Column(name = "plazas_ocupadas", nullable = false)
    private int plazasOcupadas;
}
