package dam.mod.centroplus.controller;

import dam.mod.centroplus.dto.UsuarioDTO;
import dam.mod.centroplus.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService service;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(
            @RequestBody UsuarioDTO dto,
            @RequestParam String password) {
        return ResponseEntity.ok(service.create(dto, password));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(
            @PathVariable int id,
            @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}