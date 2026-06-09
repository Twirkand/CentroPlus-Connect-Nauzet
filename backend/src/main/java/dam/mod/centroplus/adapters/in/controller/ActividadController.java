package dam.mod.centroplus.adapters.in.controller;

import dam.mod.centroplus.adapters.in.api.ActividadRequest;
import dam.mod.centroplus.adapters.in.api.ActividadResponse;
import dam.mod.centroplus.adapters.mapper.ActividadMapper;
import dam.mod.centroplus.business.ActividadServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Actividades", description = "Gestión de actividades del centro")
@RestController
@RequestMapping("/api/v1/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadServicePort service;
    private final ActividadMapper mapper;

    @Operation(summary = "Listar todas las actividades")
    @GetMapping
    public ResponseEntity<List<ActividadResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar actividad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponse> findById(@PathVariable int id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @Operation(summary = "Buscar actividades por tipo (DEPORTIVA / ACADEMICA)")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ActividadResponse>> findByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.findByTipo(tipo).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar actividades por nombre (búsqueda parcial)")
    @GetMapping("/buscar")
    public ResponseEntity<List<ActividadResponse>> findByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(service.findByNombre(nombre).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar actividades por precio máximo")
    @GetMapping("/precio")
    public ResponseEntity<List<ActividadResponse>> findByPrecio(@RequestParam double max) {
        return ResponseEntity.ok(service.findByPrecioMaximo(max).stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Crear nueva actividad")
    @PostMapping
    public ResponseEntity<ActividadResponse> create(@RequestBody ActividadRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.create(mapper.toDomain(request))));
    }

    @Operation(summary = "Actualizar actividad existente")
    @PutMapping("/{id}")
    public ResponseEntity<ActividadResponse> update(@PathVariable int id, @RequestBody ActividadRequest request) {
        return ResponseEntity.ok(mapper.toResponse(service.update(id, mapper.toDomain(request))));
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
