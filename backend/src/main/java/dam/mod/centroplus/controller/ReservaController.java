package dam.mod.centroplus.controller;

import dam.mod.centroplus.dto.ReservaDTO;
import dam.mod.centroplus.service.IReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final IReservaService service;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ReservaDTO>> findByUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(service.findByIdUsuario(idUsuario));
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> create(@RequestBody ReservaDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> update(
            @PathVariable int id,
            @RequestBody ReservaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Boolean> cambiarEstado(
            @PathVariable int id,
            @RequestParam String estado) {
        return ResponseEntity.ok(service.cambiarEstado(id, estado));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Boolean> cancelar(
            @PathVariable int id,
            @RequestParam int idUsuario) {
        return ResponseEntity.ok(service.cancelarReserva(id, idUsuario));
    }
}