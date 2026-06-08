package dam.mod.centroplus.controller;

import dam.mod.centroplus.dto.ActividadDTO;
import dam.mod.centroplus.service.IActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final IActividadService service;

    @GetMapping
    public ResponseEntity<List<ActividadDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ActividadDTO> create(@RequestBody ActividadDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadDTO> update(
            @PathVariable int id,
            @RequestBody ActividadDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints extra de negocio
    @PatchMapping("/{id}/reservar-plaza")
    public ResponseEntity<Boolean> reservarPlaza(@PathVariable int id) {
        return ResponseEntity.ok(service.reservarPlaza(id));
    }

    @PatchMapping("/{id}/cancelar-plaza")
    public ResponseEntity<Boolean> cancelarPlaza(@PathVariable int id) {
        return ResponseEntity.ok(service.cancelarPlaza(id));
    }
}