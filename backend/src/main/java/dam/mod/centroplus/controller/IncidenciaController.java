package dam.mod.centroplus.controller;

import dam.mod.centroplus.dto.IncidenciaDTO;
import dam.mod.centroplus.service.IIncidenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidencias")
@RequiredArgsConstructor
public class IncidenciaController {

    private final IIncidenciaService service;

    @GetMapping
    public ResponseEntity<List<IncidenciaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidenciaDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Incidencias de un usuario concreto
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<IncidenciaDTO>> findByUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(service.findByIdUsuario(idUsuario));
    }

    @PostMapping
    public ResponseEntity<IncidenciaDTO> create(@RequestBody IncidenciaDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidenciaDTO> update(
            @PathVariable int id,
            @RequestBody IncidenciaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Cambiar estado (ABIERTA / EN_PROCESO / CERRADA)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Boolean> cambiarEstado(
            @PathVariable int id,
            @RequestParam String estado) {
        return ResponseEntity.ok(service.cambiarEstado(id, estado));
    }
}