package dam.mod.centroplus.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "id_usuario", nullable = false)
    private int idUsuario;
    @Column(name = "id_actividad", nullable = false)
    private int idActividad;
    @Column(nullable = false)
    private String fecha;
    @Column(nullable = false)
    private String estado;
}
