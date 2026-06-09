package dam.mod.centroplus.controller;

import dam.mod.centroplus.dto.ReservaDTO;
import dam.mod.centroplus.service.IReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Reservas", description = "Gestión de reservas del centro")
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final IReservaService service;

    @Operation(summary = "Listar todas las reservas")
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar reservas de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ReservaDTO>> findByUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(service.findByIdUsuario(idUsuario));
    }

    @Operation(summary = "Listar reservas de una actividad")
    @GetMapping("/actividad/{idActividad}")
    public ResponseEntity<List<ReservaDTO>> findByActividad(@PathVariable int idActividad) {
        return ResponseEntity.ok(service.findByIdActividad(idActividad));
    }

    @Operation(summary = "Listar reservas por estado (ACTIVA / CANCELADA / COMPLETADA)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaDTO>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @Operation(summary = "Crear nueva reserva")
    @PostMapping
    public ResponseEntity<ReservaDTO> create(@RequestBody ReservaDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Actualizar reserva existente")
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> update(@PathVariable int id, @RequestBody ReservaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
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