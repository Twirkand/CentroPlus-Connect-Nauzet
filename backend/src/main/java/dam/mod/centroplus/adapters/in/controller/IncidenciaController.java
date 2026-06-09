package dam.mod.centroplus.adapters.in.controller;

import dam.mod.centroplus.adapters.in.api.IncidenciaRequest;
import dam.mod.centroplus.adapters.in.api.IncidenciaResponse;
import dam.mod.centroplus.adapters.mapper.IncidenciaMapper;
import dam.mod.centroplus.business.IncidenciaServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Incidencias", description = "Gestión de incidencias del centro")
@RequestMapping("/api/v1/incidencias")
@RequiredArgsConstructor
public class IncidenciaController {

    private final IncidenciaServicePort service;
    private final IncidenciaMapper mapper;

    @Operation(summary = "Listar todas las incidencias")
    @GetMapping
    public ResponseEntity<List<IncidenciaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar incidencia por ID")
    @GetMapping("/{id}")
    public ResponseEntity<IncidenciaResponse> findById(@PathVariable int id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @Operation(summary = "Listar incidencias de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<IncidenciaResponse>> findByUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(service.findByIdUsuario(idUsuario).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Listar incidencias por estado (ABIERTA / EN_PROCESO / CERRADA)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<IncidenciaResponse>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.findByEstado(estado).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar incidencias por asunto (búsqueda parcial)")
    @GetMapping("/buscar")
    public ResponseEntity<List<IncidenciaResponse>> findByAsunto(@RequestParam String asunto) {
        return ResponseEntity.ok(service.findByAsunto(asunto).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Crear nueva incidencia")
    @PostMapping
    public ResponseEntity<IncidenciaResponse> create(@RequestBody IncidenciaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.create(mapper.toDomain(request))));
    }

    @Operation(summary = "Actualizar incidencia existente")
    @PutMapping("/{id}")
    public ResponseEntity<IncidenciaResponse> update(@PathVariable int id, @RequestBody IncidenciaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.update(id, mapper.toDomain(request))));
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
