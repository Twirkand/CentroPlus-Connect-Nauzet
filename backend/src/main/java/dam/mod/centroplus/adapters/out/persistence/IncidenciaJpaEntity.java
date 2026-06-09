package dam.mod.centroplus.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "incidencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidenciaJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "id_usuario", nullable = false)
    private int idUsuario;
    @Column(nullable = false)
    private String asunto;
    @Column(nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private String fecha;
    @Column(nullable = false)
    private String estado;
}
