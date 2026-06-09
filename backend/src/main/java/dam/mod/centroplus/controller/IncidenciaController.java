package dam.mod.centroplus.controller;

import dam.mod.centroplus.dto.IncidenciaDTO;
import dam.mod.centroplus.service.IIncidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Incidencias", description = "Gestión de incidencias del centro")
@RequestMapping("/api/v1/incidencias")
@RequiredArgsConstructor
public class IncidenciaController {

    private final IIncidenciaService service;

    @Operation(summary = "Listar todas las incidencias")
    @GetMapping
    public ResponseEntity<List<IncidenciaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar incidencia por ID")
    @GetMapping("/{id}")
    public ResponseEntity<IncidenciaDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar incidencias de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<IncidenciaDTO>> findByUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(service.findByIdUsuario(idUsuario));
    }

    @Operation(summary = "Listar incidencias por estado (ABIERTA / EN_PROCESO / CERRADA)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<IncidenciaDTO>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @Operation(summary = "Buscar incidencias por asunto (búsqueda parcial)")
    @GetMapping("/buscar")
    public ResponseEntity<List<IncidenciaDTO>> findByAsunto(@RequestParam String asunto) {
        return ResponseEntity.ok(service.findByAsunto(asunto));
    }

    @Operation(summary = "Crear nueva incidencia")
    @PostMapping
    public ResponseEntity<IncidenciaDTO> create(@RequestBody IncidenciaDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Actualizar incidencia existente")
    @PutMapping("/{id}")
    public ResponseEntity<IncidenciaDTO> update(@PathVariable int id, @RequestBody IncidenciaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Eliminar incidencia")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar estado de incidencia (ABIERTA / EN_PROCESO / CERRADA)")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Boolean> cambiarEstado(@PathVariable int id, @RequestParam String estado) {
        return ResponseEntity.ok(service.cambiarEstado(id, estado));
    }
}