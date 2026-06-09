package dam.mod.centroplus.adapters.in.controller;

import dam.mod.centroplus.adapters.out.persistence.UsuarioJpaEntity;
import dam.mod.centroplus.adapters.out.persistence.UsuarioPersistenceAdapter;
import dam.mod.centroplus.infrastructure.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Autenticación", description = "Login y gestión de tokens JWT")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioPersistenceAdapter usuarioAdapter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Iniciar sesión y obtener token JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UsuarioJpaEntity usuario = usuarioAdapter.findEntityByDni(request.getDni())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtils.generateToken(usuario.getDni(), usuario.getTipoUsuario());
        return ResponseEntity.ok(new LoginResponse(token, usuario.getTipoUsuario(), usuario.getNombre()));
    }

    @Data
    public static class LoginRequest {
        private String dni;
        private String password;
    }

    @Data
    public static class LoginResponse {
        private final String token;
        private final String tipoUsuario;
        private final String nombre;
    }
}
