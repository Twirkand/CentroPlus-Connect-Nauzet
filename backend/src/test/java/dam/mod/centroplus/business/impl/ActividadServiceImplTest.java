package dam.mod.centroplus.business.impl;

import dam.mod.centroplus.adapters.out.persistence.ActividadPersistenceAdapter;
import dam.mod.centroplus.domain.model.Actividad;
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
@DisplayName("ActividadServiceImpl")
class ActividadServiceImplTest {

    @Mock
    private ActividadPersistenceAdapter persistenceAdapter;

    @InjectMocks
    private ActividadServiceImpl service;

    private Actividad actividadValida;

    @BeforeEach
    void setUp() {
        actividadValida = new Actividad(1, "Yoga", "DEPORTIVA", 60, 15.0, 20, 5);
    }

    @Test
    @DisplayName("findAll devuelve la lista del adaptador")
    void findAll_devuelveLista() {
        when(persistenceAdapter.findAll()).thenReturn(List.of(actividadValida));

        List<Actividad> result = service.findAll();

        assertThat(result).hasSize(1).contains(actividadValida);
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("devuelve la actividad cuando existe")
        void findById_existente() {
            when(persistenceAdapter.findById(1)).thenReturn(Optional.of(actividadValida));

            Actividad result = service.findById(1);

            assertThat(result).isEqualTo(actividadValida);
        }

        @Test
        @DisplayName("lanza RuntimeException cuando no existe")
        void findById_noExistente() {
            when(persistenceAdapter.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(99))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("findByTipo")
    class FindByTipo {

        @Test
        @DisplayName("DEPORTIVA es válido")
        void findByTipo_deportiva() {
            when(persistenceAdapter.findByTipoActividad("DEPORTIVA")).thenReturn(List.of(actividadValida));

            assertThat(service.findByTipo("DEPORTIVA")).hasSize(1);
        }

        @Test
        @DisplayName("ACADEMICA es válido")
        void findByTipo_academica() {
            Actividad academica = new Actividad(2, "Matemáticas", "ACADEMICA", 90, 10.0, 15, 0);
            when(persistenceAdapter.findByTipoActividad("ACADEMICA")).thenReturn(List.of(academica));

            assertThat(service.findByTipo("ACADEMICA")).hasSize(1);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con tipo inválido")
        void findByTipo_invalido() {
            assertThatThrownBy(() -> service.findByTipo("INVALIDO"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con tipo null")
        void findByTipo_null() {
            assertThatThrownBy(() -> service.findByTipo(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("findByNombre")
    class FindByNombre {

        @Test
        @DisplayName("busca por nombre correctamente")
        void findByNombre_ok() {
            when(persistenceAdapter.findByNombreContainingIgnoreCase("yoga"))
                    .thenReturn(List.of(actividadValida));

            assertThat(service.findByNombre("yoga")).hasSize(1);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con nombre en blanco")
        void findByNombre_blanco() {
            assertThatThrownBy(() -> service.findByNombre("  "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con nombre null")
        void findByNombre_null() {
            assertThatThrownBy(() -> service.findByNombre(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("findByPrecioMaximo")
    class FindByPrecioMaximo {

        @Test
        @DisplayName("precio positivo funciona correctamente")
        void findByPrecioMaximo_ok() {
            when(persistenceAdapter.findByPrecioLessThanEqual(20.0)).thenReturn(List.of(actividadValida));

            assertThat(service.findByPrecioMaximo(20.0)).hasSize(1);
        }

        @Test
        @DisplayName("precio cero es válido (actividades gratis)")
        void findByPrecioMaximo_cero() {
            when(persistenceAdapter.findByPrecioLessThanEqual(0.0)).thenReturn(List.of());

            assertThat(service.findByPrecioMaximo(0.0)).isEmpty();
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con precio negativo")
        void findByPrecioMaximo_negativo() {
            assertThatThrownBy(() -> service.findByPrecioMaximo(-1.0))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("crea actividad válida con plazasOcupadas=0")
        void create_valida() {
            Actividad nueva = new Actividad(0, "Pilates", "DEPORTIVA", 45, 12.0, 10, 3);
            Actividad guardada = new Actividad(5, "Pilates", "DEPORTIVA", 45, 12.0, 10, 0);
            when(persistenceAdapter.save(any())).thenReturn(guardada);

            Actividad result = service.create(nueva);

            assertThat(result.getPlazasOcupadas()).isZero();
            verify(persistenceAdapter).save(any());
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con nombre null")
        void create_nombreNull() {
            Actividad invalida = new Actividad(0, null, "DEPORTIVA", 60, 10.0, 10, 0);

            assertThatThrownBy(() -> service.create(invalida))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("nombre");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con tipo inválido")
        void create_tipoInvalido() {
            Actividad invalida = new Actividad(0, "Test", "OTRO", 60, 10.0, 10, 0);

            assertThatThrownBy(() -> service.create(invalida))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con duración <= 0")
        void create_duracionCero() {
            Actividad invalida = new Actividad(0, "Test", "DEPORTIVA", 0, 10.0, 10, 0);

            assertThatThrownBy(() -> service.create(invalida))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("duración");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con precio negativo")
        void create_precioNegativo() {
            Actividad invalida = new Actividad(0, "Test", "DEPORTIVA", 60, -1.0, 10, 0);

            assertThatThrownBy(() -> service.create(invalida))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("precio");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con plazasMaximas <= 0")
        void create_plazasMaximasCero() {
            Actividad invalida = new Actividad(0, "Test", "DEPORTIVA", 60, 10.0, 0, 0);

            assertThatThrownBy(() -> service.create(invalida))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("plazas máximas");
        }
    }

    @Test
    @DisplayName("update actualiza actividad existente")
    void update_ok() {
        Actividad actualizada = new Actividad(0, "Yoga Avanzado", "DEPORTIVA", 90, 20.0, 15, 0);
        when(persistenceAdapter.findById(1)).thenReturn(Optional.of(actividadValida));
        when(persistenceAdapter.save(any())).thenReturn(actualizada);

        service.update(1, actualizada);

        verify(persistenceAdapter).save(any());
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("elimina actividad existente")
        void delete_existente() {
            when(persistenceAdapter.existsById(1)).thenReturn(true);

            service.delete(1);

            verify(persistenceAdapter).deleteById(1);
        }

        @Test
        @DisplayName("lanza RuntimeException si no existe")
        void delete_noExistente() {
            when(persistenceAdapter.existsById(99)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(99))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("reservarPlaza")
    class ReservarPlaza {

        @Test
        @DisplayName("incrementa plazas ocupadas")
        void reservar_ok() {
            Actividad actividad = new Actividad(1, "Yoga", "DEPORTIVA", 60, 15.0, 20, 5);
            when(persistenceAdapter.findById(1)).thenReturn(Optional.of(actividad));
            when(persistenceAdapter.save(any())).thenReturn(actividad);

            boolean result = service.reservarPlaza(1);

            assertThat(result).isTrue();
            assertThat(actividad.getPlazasOcupadas()).isEqualTo(6);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException cuando está llena")
        void reservar_sinPlazas() {
            Actividad llena = new Actividad(1, "Yoga", "DEPORTIVA", 60, 15.0, 5, 5);
            when(persistenceAdapter.findById(1)).thenReturn(Optional.of(llena));

            assertThatThrownBy(() -> service.reservarPlaza(1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("plazas");
        }
    }

    @Nested
    @DisplayName("cancelarPlaza")
    class CancelarPlaza {

        @Test
        @DisplayName("decrementa plazas ocupadas")
        void cancelar_ok() {
            Actividad actividad = new Actividad(1, "Yoga", "DEPORTIVA", 60, 15.0, 20, 3);
            when(persistenceAdapter.findById(1)).thenReturn(Optional.of(actividad));
            when(persistenceAdapter.save(any())).thenReturn(actividad);

            boolean result = service.cancelarPlaza(1);

            assertThat(result).isTrue();
            assertThat(actividad.getPlazasOcupadas()).isEqualTo(2);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException cuando no hay plazas ocupadas")
        void cancelar_sinPlazasOcupadas() {
            Actividad vacia = new Actividad(1, "Yoga", "DEPORTIVA", 60, 15.0, 20, 0);
            when(persistenceAdapter.findById(1)).thenReturn(Optional.of(vacia));

            assertThatThrownBy(() -> service.cancelarPlaza(1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}