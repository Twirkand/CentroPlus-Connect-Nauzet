package dam.mod.centroplus.adapters.mapper;

import dam.mod.centroplus.adapters.in.api.UsuarioRequest;
import dam.mod.centroplus.adapters.in.api.UsuarioResponse;
import dam.mod.centroplus.adapters.out.persistence.UsuarioJpaEntity;
import dam.mod.centroplus.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toDomain(UsuarioJpaEntity entity);

    @Mapping(target = "password", ignore = true)
    UsuarioJpaEntity toJpaEntity(Usuario domain);

    @Mapping(target = "id", ignore = true)
    Usuario toDomain(UsuarioRequest request);

    UsuarioResponse toResponse(Usuario domain);
}
