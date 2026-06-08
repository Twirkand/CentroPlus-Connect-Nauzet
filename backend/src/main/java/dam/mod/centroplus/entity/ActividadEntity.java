package dam.mod.centroplus.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "actividades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadEntity {

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