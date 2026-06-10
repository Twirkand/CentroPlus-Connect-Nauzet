package dam.mod.centroplus.business.impl;

import dam.mod.centroplus.adapters.out.persistence.ActividadPersistenceAdapter;
import dam.mod.centroplus.adapters.out.persistence.ReservaPersistenceAdapter;
import dam.mod.centroplus.adapters.out.persistence.UsuarioPersistenceAdapter;
import dam.mod.centroplus.business.ActividadServicePort;
import dam.mod.centroplus.domain.model.Actividad;
import dam.mod.centroplus.domain.model.Reserva;
import dam.mod.centroplus.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReservaServiceImpl")
class ReservaServiceImplTest {

    @Mock
    private ReservaPersistenceAdapter reservaAdapter;
    @Mock
    private UsuarioPersistenceAdapter usuarioAdapter;
    @Mock
    private ActividadPersistenceAdapter actividadAdapter;
    @Mock
    private ActividadServicePort actividadService;

    @InjectMocks
    private ReservaServiceImpl service;

    private Reserva reservaActiva;
    private Actividad actividad;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        reservaActiva = new Reserva(1, 10, 5, "2025-01-01", "ACTIVA", null);
        actividad = new Actividad(5, "Yoga", "DEPORTIVA", 60, 15.0, 20, 3);
        usuario = new Usuario(10, "Ana García", "12345678A", "ana@email.com", "600111222", "SOCIO");
    }

    @Test
    @DisplayName("findAll devuelve lista enriquecida con nombre de actividad")
    void findAll_enriquece() {
        when(reservaAdapter.findAll()).thenReturn(List.of(reservaActiva));
        when(actividadAdapter.findById(5)).thenReturn(Optional.of(actividad));

        List<Reserva> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreActividad()).isEqualTo("Yoga");
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("devuelve reserva existente")
        void findById_existente() {
            when(reservaAdapter.findById(1)).thenReturn(Optional.of(reservaActiva));
            when(actividadAdapter.findById(5)).thenReturn(Optional.of(actividad));

            Reserva result = service.findById(1);

            assertThat(result.getId()).isEqualTo(1);
        }

        @Test
        @DisplayName("lanza RuntimeException si no existe")
        void findById_noExistente() {
            when(reservaAdapter.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(99))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("findByEstado")
    class FindByEstado {

        @Test
        @DisplayName("ACTIVA es válido")
        void findByEstado_activa() {
            when(reservaAdapter.findByEstado("ACTIVA")).thenReturn(List.of(reservaActiva));
            when(actividadAdapter.findById(anyInt())).thenReturn(Optional.of(actividad));

            assertThat(service.findByEstado("ACTIVA")).hasSize(1);
        }

        @Test
        @DisplayName("CANCELADA es válido")
        void findByEstado_cancelada() {
            when(reservaAdapter.findByEstado("CANCELADA")).thenReturn(List.of());

            assertThat(service.findByEstado("CANCELADA")).isEmpty();
        }

        @Test
        @DisplayName("COMPLETADA es válido")
        void findByEstado_completada() {
            when(reservaAdapter.findByEstado("COMPLETADA")).thenReturn(List.of());

            assertThat(service.findByEstado("COMPLETADA")).isEmpty();
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con estado inválido")
        void findByEstado_invalido() {
            assertThatThrownBy(() -> service.findByEstado("PENDIENTE"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("crea reserva correctamente cuando todo es válido")
        void create_ok() {
            Reserva nueva = new Reserva(0, 10, 5, null, null, null);
            Reserva guardada = new Reserva(1, 10, 5, "2025-06-10", "ACTIVA", null);

            when(usuarioAdapter.findById(10)).thenReturn(Optional.of(usuario));
            when(actividadAdapter.findById(5)).thenReturn(Optional.of(actividad));
            when(reservaAdapter.existsByIdActividadAndIdUsuarioAndEstado(5, 10, "ACTIVA")).thenReturn(false);
            when(reservaAdapter.save(any())).thenReturn(guardada);
            when(actividadAdapter.findById(5)).thenReturn(Optional.of(actividad)); // enrich

            Reserva result = service.create(nueva);

            assertThat(result.getEstado()).isEqualTo("ACTIVA");
            verify(actividadService).reservarPlaza(5);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException si el usuario no existe")
        void create_usuarioNoExiste() {
            Reserva nueva = new Reserva(0, 99, 5, null, null, null);
            when(usuarioAdapter.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.create(nueva))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("usuario");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException si la actividad no existe")
        void create_actividadNoExiste() {
            Reserva nueva = new Reserva(0, 10, 99, null, null, null);
            when(usuarioAdapter.findById(10)).thenReturn(Optional.of(usuario));
            when(actividadAdapter.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.create(nueva))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("actividad");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException si ya existe reserva activa")
        void create_yaReservado() {
            Reserva nueva = new Reserva(0, 10, 5, null, null, null);
            when(usuarioAdapter.findById(10)).thenReturn(Optional.of(usuario));
            when(actividadAdapter.findById(5)).thenReturn(Optional.of(actividad));
            when(reservaAdapter.existsByIdActividadAndIdUsuarioAndEstado(5, 10, "ACTIVA")).thenReturn(true);

            assertThatThrownBy(() -> service.create(nueva))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("reserva activa");
        }
    }

    @Nested
    @DisplayName("cambiarEstado")
    class CambiarEstado {

        @Test
        @DisplayName("cancelar una reserva activa libera plaza")
        void cambiarEstado_aCancelada() {
            when(reservaAdapter.findById(1)).thenReturn(Optional.of(reservaActiva));
            when(actividadAdapter.findById(anyInt())).thenReturn(Optional.of(actividad));
            when(reservaAdapter.save(any())).thenReturn(reservaActiva);

            service.cambiarEstado(1, "CANCELADA");

            verify(actividadService).cancelarPlaza(5);
        }

        @Test
        @DisplayName("reactivar una reserva cancelada ocupa plaza")
        void cambiarEstado_aActiva() {
            Reserva cancelada = new Reserva(1, 10, 5, "2025-01-01", "CANCELADA", null);
            when(reservaAdapter.findById(1)).thenReturn(Optional.of(cancelada));
            when(actividadAdapter.findById(anyInt())).thenReturn(Optional.of(actividad));
            when(reservaAdapter.save(any())).thenReturn(cancelada);

            service.cambiarEstado(1, "ACTIVA");

            verify(actividadService).reservarPlaza(5);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con estado inválido")
        void cambiarEstado_invalido() {
            when(reservaAdapter.findById(1)).thenReturn(Optional.of(reservaActiva));
            when(actividadAdapter.findById(anyInt())).thenReturn(Optional.of(actividad));

            assertThatThrownBy(() -> service.cambiarEstado(1, "INVALIDO"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("cancelarReserva")
    class CancelarReserva {

        @Test
        @DisplayName("cancela y borra la reserva del usuario correcto")
        void cancelar_ok() {
            when(reservaAdapter.findById(1)).thenReturn(Optional.of(reservaActiva));
            when(actividadAdapter.findById(5)).thenReturn(Optional.of(actividad));

            boolean result = service.cancelarReserva(1, 10);

            assertThat(result).isTrue();
            verify(actividadService).cancelarPlaza(5);
            verify(reservaAdapter).deleteById(1);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException si la reserva no pertenece al usuario")
        void cancelar_usuarioIncorrecto() {
            when(reservaAdapter.findById(1)).thenReturn(Optional.of(reservaActiva));
            when(actividadAdapter.findById(5)).thenReturn(Optional.of(actividad));

            assertThatThrownBy(() -> service.cancelarReserva(1, 999))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("usuario");
        }
    }

    @Test
    @DisplayName("delete lanza RuntimeException si la reserva no existe")
    void delete_noExistente() {
        when(reservaAdapter.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99))
                .isInstanceOf(RuntimeException.class);
    }
}