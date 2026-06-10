package dam.mod.centroplus.business.impl;

import dam.mod.centroplus.adapters.mapper.UsuarioMapper;
import dam.mod.centroplus.adapters.out.persistence.UsuarioJpaEntity;
import dam.mod.centroplus.adapters.out.persistence.UsuarioPersistenceAdapter;
import dam.mod.centroplus.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioServiceImpl")
class UsuarioServiceImplTest {

    @Mock
    private UsuarioPersistenceAdapter persistenceAdapter;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private UsuarioServiceImpl service;

    private Usuario usuarioValido;
    private UsuarioJpaEntity entityValida;

    @BeforeEach
    void setUp() {
        usuarioValido = new Usuario(1, "Ana García", "12345678A", "ana@email.com", "600111222", "SOCIO");
        entityValida = new UsuarioJpaEntity(1, "Ana García", "12345678A", "ana@email.com", "600111222", "SOCIO",
                "hashed");
    }

    @Test
    @DisplayName("findAll devuelve la lista del adaptador")
    void findAll_ok() {
        when(persistenceAdapter.findAll()).thenReturn(List.of(usuarioValido));

        assertThat(service.findAll()).hasSize(1);
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("devuelve usuario existente")
        void findById_existente() {
            when(persistenceAdapter.findById(1)).thenReturn(Optional.of(usuarioValido));

            assertThat(service.findById(1)).isEqualTo(usuarioValido);
        }

        @Test
        @DisplayName("lanza RuntimeException si no existe")
        void findById_noExistente() {
            when(persistenceAdapter.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(99))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("findByDni")
    class FindByDni {

        @Test
        @DisplayName("devuelve usuario con ese DNI")
        void findByDni_existente() {
            when(persistenceAdapter.findByDni("12345678A")).thenReturn(Optional.of(usuarioValido));

            assertThat(service.findByDni("12345678A")).isEqualTo(usuarioValido);
        }

        @Test
        @DisplayName("lanza RuntimeException si no existe")
        void findByDni_noExistente() {
            when(persistenceAdapter.findByDni("00000000Z")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findByDni("00000000Z"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("00000000Z");
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("crea usuario válido con password hasheado y DNI en mayúsculas")
        void create_ok() {
            when(persistenceAdapter.existsByDni(anyString())).thenReturn(false);
            when(persistenceAdapter.existsByEmail(anyString())).thenReturn(false);
            when(mapper.toJpaEntity(any())).thenReturn(entityValida);
            when(passwordEncoder.encode("secret123")).thenReturn("hashed_secret");
            when(persistenceAdapter.saveEntity(any())).thenReturn(entityValida);
            when(mapper.toDomain(entityValida)).thenReturn(usuarioValido);

            Usuario result = service.create(usuarioValido, "secret123");

            assertThat(result).isNotNull();
            verify(passwordEncoder).encode("secret123");
            verify(persistenceAdapter).saveEntity(any());
        }

        @Test
        @DisplayName("normaliza el DNI a mayúsculas antes de comprobar duplicado y guardar")
        void create_dniMayusculas() {
            Usuario conDniMinusculas = new Usuario(0, "Ana", "12345678a", "ana@email.com", "600", "SOCIO");
            when(persistenceAdapter.existsByDni("12345678A")).thenReturn(false);
            when(persistenceAdapter.existsByEmail(anyString())).thenReturn(false);
            when(mapper.toJpaEntity(any())).thenReturn(entityValida);
            when(passwordEncoder.encode(anyString())).thenReturn("hash");
            when(persistenceAdapter.saveEntity(any())).thenReturn(entityValida);
            when(mapper.toDomain(entityValida)).thenReturn(usuarioValido);

            service.create(conDniMinusculas, "password123");

            verify(persistenceAdapter).existsByDni("12345678A");
            verify(persistenceAdapter).saveEntity(argThat(e -> "12345678A".equals(e.getDni())));
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con nombre null")
        void create_nombreNull() {
            Usuario invalido = new Usuario(0, null, "12345678A", "email@test.com", null, "SOCIO");

            assertThatThrownBy(() -> service.create(invalido, "password123"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("nombre");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con nombre en blanco")
        void create_nombreBlanco() {
            Usuario invalido = new Usuario(0, "  ", "12345678A", "email@test.com", null, "SOCIO");

            assertThatThrownBy(() -> service.create(invalido, "password123"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con DNI null")
        void create_dniNull() {
            Usuario invalido = new Usuario(0, "Ana", null, "email@test.com", null, "SOCIO");

            assertThatThrownBy(() -> service.create(invalido, "password123"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("DNI");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con email null")
        void create_emailNull() {
            Usuario invalido = new Usuario(0, "Ana", "12345678A", null, null, "SOCIO");

            assertThatThrownBy(() -> service.create(invalido, "password123"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("email");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con password menor de 6 caracteres")
        void create_passwordCorta() {
            assertThatThrownBy(() -> service.create(usuarioValido, "abc"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("contraseña");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException con password null")
        void create_passwordNull() {
            assertThatThrownBy(() -> service.create(usuarioValido, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("lanza IllegalArgumentException si el DNI ya existe")
        void create_dniDuplicado() {
            when(persistenceAdapter.existsByDni("12345678A")).thenReturn(true);

            assertThatThrownBy(() -> service.create(usuarioValido, "password123"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("DNI");
        }

        @Test
        @DisplayName("lanza IllegalArgumentException si el email ya existe")
        void create_emailDuplicado() {
            when(persistenceAdapter.existsByDni(anyString())).thenReturn(false);
            when(persistenceAdapter.existsByEmail("ana@email.com")).thenReturn(true);

            assertThatThrownBy(() -> service.create(usuarioValido, "password123"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("email");
        }
    }

    @Test
    @DisplayName("update modifica nombre, email, teléfono y tipo")
    void update_ok() {
        Usuario cambios = new Usuario(0, "Ana Actualizada", "ana_new@email.com", null, "611222333", "ADMIN");
        when(persistenceAdapter.findEntityById(1)).thenReturn(Optional.of(entityValida));
        when(persistenceAdapter.saveEntity(any())).thenReturn(entityValida);
        when(mapper.toDomain(entityValida)).thenReturn(usuarioValido);

        service.update(1, cambios);

        verify(persistenceAdapter).saveEntity(any());
    }

    @Test
    @DisplayName("update lanza RuntimeException si el usuario no existe")
    void update_noExistente() {
        when(persistenceAdapter.findEntityById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99, usuarioValido))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("elimina usuario existente")
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
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("99");
        }
    }
}