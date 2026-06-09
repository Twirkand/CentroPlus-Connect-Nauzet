package dam.mod.centroplus.adapters.in.controller;

import dam.mod.centroplus.adapters.in.api.ReservaRequest;
import dam.mod.centroplus.adapters.in.api.ReservaResponse;
import dam.mod.centroplus.adapters.mapper.ReservaMapper;
import dam.mod.centroplus.business.ReservaServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Reservas", description = "Gestión de reservas del centro")
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaServicePort service;
    private final ReservaMapper mapper;

    @Operation(summary = "Listar todas las reservas")
    @GetMapping
    public ResponseEntity<List<ReservaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> findById(@PathVariable int id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @Operation(summary = "Listar reservas de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ReservaResponse>> findByUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(service.findByIdUsuario(idUsuario).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Listar reservas de una actividad")
    @GetMapping("/actividad/{idActividad}")
    public ResponseEntity<List<ReservaResponse>> findByActividad(@PathVariable int idActividad) {
        return ResponseEntity.ok(service.findByIdActividad(idActividad).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Listar reservas por estado (ACTIVA / CANCELADA / COMPLETADA)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaResponse>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.findByEstado(estado).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Crear nueva reserva")
    @PostMapping
    public ResponseEntity<ReservaResponse> create(@RequestBody ReservaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.create(mapper.toDomain(request))));
    }

    @Operation(summary = "Actualizar reserva existente")
    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> update(@PathVariable int id, @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.update(id, mapper.toDomain(request))));
    }

    @Operation(summary = "Eliminar reserva")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar estado de reserva (ACTIVA / CANCELADA / COMPLETADA)")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Boolean> cambiarEstado(@PathVariable int id, @RequestParam String estado) {
        return ResponseEntity.ok(service.cambiarEstado(id, estado));
    }

    @Operation(summary = "Cancelar reserva de un usuario")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Boolean> cancelar(@PathVariable int id, @RequestParam int idUsuario) {
        return ResponseEntity.ok(service.cancelarReserva(id, idUsuario));
    }
}
