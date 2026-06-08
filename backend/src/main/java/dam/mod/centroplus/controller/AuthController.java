package dam.mod.centroplus.controller;

import dam.mod.centroplus.dto.LoginRequest;
import dam.mod.centroplus.dto.LoginResponse;
import dam.mod.centroplus.entity.UsuarioEntity;
import dam.mod.centroplus.repository.UsuarioRepository;
import dam.mod.centroplus.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByDni(request.getDni())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtils.generateToken(usuario.getDni(), usuario.getTipoUsuario());
        return ResponseEntity.ok(new LoginResponse(token, usuario.getTipoUsuario(), usuario.getNombre()));
    }
}