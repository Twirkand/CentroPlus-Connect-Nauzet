package dam.mod.centroplus.business.impl;

import dam.mod.centroplus.adapters.out.persistence.IncidenciaPersistenceAdapter;
import dam.mod.centroplus.adapters.out.persistence.UsuarioPersistenceAdapter;
import dam.mod.centroplus.domain.model.Incidencia;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IncidenciaServiceImpl")
class IncidenciaServiceImplTest {

    @Mock
    private IncidenciaPersistenceAdapter incidenciaAdapter;
    @Mock
    private UsuarioPersistenceAdapter usuarioAdapter;

    @InjectMocks
    private IncidenciaServiceImpl service;

    private Incidencia incidenciaValida;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        incidenciaValida = new Incidencia(1, 10, "Luz averiada", "La luz del pasillo no funciona", "2025-01-01",
                "ABIERTA");
        usuario = new Usuario(10, "Ana García", "12345678A", "ana@email.com", "600111222", "SOCIO");
    }

    @Test
    @DisplayName("findAll devuelve la lista del adaptador")
    void findAll_ok() {
        when(incidenciaAdapter.findAll()).thenReturn(List.of(incidenciaValida));

        assertThat(service.findAll()).hasSize(1);
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("devuelve incidencia existente")
        void findById_existente() {
            when(incidenciaAdapter.findById(1)).thenReturn(Optional.of(incidenciaValida));

            assertThat(service.findById(1)).isEqualTo(incidenciaValida);
        }

        @Test
        @DisplayName("lanza RuntimeException si no existe")
        void findById_noExistente() {
            when(incidenciaAdapter.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(99))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("findByEstado")
    class FindByEstado {

        @Test
        @DisplayName("ABIERTA es válido")
        void findByEstado_abierta() {
            when(incidenciaAdapter.findByEstado("ABIERTA")).thenReturn(List.of(incidenciaValida));
            assertThat(service.findByEstado("ABIERTA")).hasSize(1);
        }

        @Test
        @DisplayName("EN_PROCESO es válido")
        void findByEstado_enProceso() {
            when(incidenciaAdapter.findByEstado("EN_PROCESO")).thenReturn(List.of());
            assertThat(service.findByEstado("EN_PROCESO")).isEmpty();
        }

        @Test
        @DisplayName("CERRADA es válido")
        void findByEstado_cerrada() {
            when(incidenciaAdapter.findByEstado("CERRADA")).thenReturn(List.of());
            assertThat(service.findByEstado("CERRADA")).isEmpty();
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con estado inválido")
        void findByEstado_invalido() {
            assertThatThrownBy(() -> service.findByEstado("PENDIENTE"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con estado null")
        void findByEstado_null() {
            assertThatThrownBy(() -> service.findByEstado(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("crea incidencia válida con estado ABIERTA y fecha de hoy")
        void create_ok() {
            Incidencia nueva = new Incidencia(0, 10, "Grifo roto", "El grifo del baño pierde agua", null, null);
            Incidencia guardada = new Incidencia(2, 10, "Grifo roto", "El grifo del baño pierde agua", "2025-06-10",
                    "ABIERTA");

            when(usuarioAdapter.findById(10)).thenReturn(Optional.of(usuario));
            when(incidenciaAdapter.save(any())).thenReturn(guardada);

            Incidencia result = service.create(nueva);

            assertThat(result.getEstado()).isEqualTo("ABIERTA");
            verify(incidenciaAdapter).save(any());
        }

        @Test
        @DisplayName("lanza IllegalArgumentException si el usuario no existe")
        void create_usuarioNoExiste() {
            Incidencia nueva = new Incidencia(0, 99, "Asunto", "Descripcion", null, null);
            when(usuarioAdapter.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.create(nueva))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("usuario");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con asunto null")
        void create_asuntoNull() {
            Incidencia invalida = new Incidencia(0, 10, null, "Descripcion", null, null);

            assertThatThrownBy(() -> service.create(invalida))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("asunto");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con asunto en blanco")
        void create_asuntoBlanco() {
            Incidencia invalida = new Incidencia(0, 10, "   ", "Descripcion", null, null);

            assertThatThrownBy(() -> service.create(invalida))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con descripción null")
        void create_descripcionNull() {
            Incidencia invalida = new Incidencia(0, 10, "Asunto", null, null, null);

            assertThatThrownBy(() -> service.create(invalida))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("descripción");
        }
    }

    @Test
    @DisplayName("update modifica asunto, descripción y estado")
    void update_ok() {
        Incidencia cambios = new Incidencia(0, 10, "Nuevo asunto", "Nueva descripción", null, "EN_PROCESO");
        when(incidenciaAdapter.findById(1)).thenReturn(Optional.of(incidenciaValida));
        when(incidenciaAdapter.save(any())).thenReturn(incidenciaValida);

        service.update(1, cambios);

        verify(incidenciaAdapter).save(any());
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("elimina incidencia existente")
        void delete_existente() {
            when(incidenciaAdapter.existsById(1)).thenReturn(true);

            service.delete(1);

            verify(incidenciaAdapter).deleteById(1);
        }

        @Test
        @DisplayName("lanza RuntimeException si no existe")
        void delete_noExistente() {
            when(incidenciaAdapter.existsById(99)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(99))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("cambiarEstado")
    class CambiarEstado {

        @Test
        @DisplayName("cambia estado correctamente")
        void cambiarEstado_ok() {
            when(incidenciaAdapter.findById(1)).thenReturn(Optional.of(incidenciaValida));
            when(incidenciaAdapter.save(any())).thenReturn(incidenciaValida);

            boolean result = service.cambiarEstado(1, "EN_PROCESO");

            assertThat(result).isTrue();
            assertThat(incidenciaValida.getEstado()).isEqualTo("EN_PROCESO");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con estado inválido")
        void cambiarEstado_invalido() {
            when(incidenciaAdapter.findById(1)).thenReturn(Optional.of(incidenciaValida));

            assertThatThrownBy(() -> service.cambiarEstado(1, "RECHAZADA"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}