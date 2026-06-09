package dam.mod.centroplus.adapters.in.controller;

import dam.mod.centroplus.adapters.in.api.UsuarioRequest;
import dam.mod.centroplus.adapters.in.api.UsuarioResponse;
import dam.mod.centroplus.adapters.mapper.UsuarioMapper;
import dam.mod.centroplus.business.UsuarioServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Usuarios", description = "Gestión de usuarios del centro")
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServicePort service;
    private final UsuarioMapper mapper;

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable int id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @Operation(summary = "Buscar usuario por DNI")
    @GetMapping("/dni/{dni}")
    public ResponseEntity<UsuarioResponse> findByDni(@PathVariable String dni) {
        return ResponseEntity.ok(mapper.toResponse(service.findByDni(dni)));
    }

    @Operation(summary = "Buscar usuario por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponse> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(mapper.toResponse(service.findByEmail(email)));
    }

    @Operation(summary = "Buscar usuarios por tipo (ALUMNO / SOCIO / AMBOS)")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<UsuarioResponse>> findByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.findByTipo(tipo).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar usuarios por nombre (búsqueda parcial)")
    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponse>> findByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(service.findByNombre(nombre).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Crear nuevo usuario")
    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@RequestBody UsuarioRequest request, @RequestParam String password) {
        return ResponseEntity.ok(mapper.toResponse(service.create(mapper.toDomain(request), password)));
    }

    @Operation(summary = "Actualizar usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable int id, @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.update(id, mapper.toDomain(request))));
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
