package dam.mod.centroplus.infrastructure.security;

import dam.mod.centroplus.adapters.out.persistence.UsuarioJpaEntity;
import dam.mod.centroplus.adapters.out.persistence.UsuarioPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioPersistenceAdapter usuarioAdapter;

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        UsuarioJpaEntity usuario = usuarioAdapter.findEntityByDni(dni)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + dni));

        return new User(
                usuario.getDni(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario()))
        );
    }
}
