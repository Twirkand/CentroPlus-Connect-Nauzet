package dam.mod.centroplus.controller;

import dam.mod.centroplus.dto.ActividadDTO;
import dam.mod.centroplus.service.IActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Actividades", description = "Gestión de actividades del centro")
@RestController
@RequestMapping("/api/v1/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final IActividadService service;

    @Operation(summary = "Listar todas las actividades")
    @GetMapping
    public ResponseEntity<List<ActividadDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar actividad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear nueva actividad")
    @PostMapping
    public ResponseEntity<ActividadDTO> create(@RequestBody ActividadDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Actualizar actividad existente")
    @PutMapping("/{id}")
    public ResponseEntity<ActividadDTO> update(
            @PathVariable int id,
            @RequestBody ActividadDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Eliminar actividad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reservar una plaza en la actividad")
    @PatchMapping("/{id}/reservar-plaza")
    public ResponseEntity<Boolean> reservarPlaza(@PathVariable int id) {
        return ResponseEntity.ok(service.reservarPlaza(id));
    }

    @Operation(summary = "Cancelar una plaza de la actividad")
    @PatchMapping("/{id}/cancelar-plaza")
    public ResponseEntity<Boolean> cancelarPlaza(@PathVariable int id) {
        return ResponseEntity.ok(service.cancelarPlaza(id));
    }
}