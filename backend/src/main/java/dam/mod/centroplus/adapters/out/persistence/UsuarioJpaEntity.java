package dam.mod.centroplus.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String dni;
    @Column(nullable = false)
    private String email;
    private String telefono;
    @Column(name = "tipo_usuario", nullable = false)
    private String tipoUsuario;
    @Column(nullable = false)
    private String password;
}
