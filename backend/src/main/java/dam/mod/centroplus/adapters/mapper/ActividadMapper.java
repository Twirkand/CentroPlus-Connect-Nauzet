package dam.mod.centroplus.adapters.mapper;

import dam.mod.centroplus.adapters.in.api.ActividadRequest;
import dam.mod.centroplus.adapters.in.api.ActividadResponse;
import dam.mod.centroplus.adapters.out.persistence.ActividadJpaEntity;
import dam.mod.centroplus.domain.model.Actividad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActividadMapper {

    // Domain ↔ JPA
    Actividad toDomain(ActividadJpaEntity entity);
    ActividadJpaEntity toJpaEntity(Actividad domain);

    // API ↔ Domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plazasOcupadas", constant = "0")
    Actividad toDomain(ActividadRequest request);

    ActividadResponse toResponse(Actividad domain);
}
