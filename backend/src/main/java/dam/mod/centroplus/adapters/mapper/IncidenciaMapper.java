package dam.mod.centroplus.adapters.mapper;

import dam.mod.centroplus.adapters.in.api.IncidenciaRequest;
import dam.mod.centroplus.adapters.in.api.IncidenciaResponse;
import dam.mod.centroplus.adapters.out.persistence.IncidenciaJpaEntity;
import dam.mod.centroplus.domain.model.Incidencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncidenciaMapper {

    Incidencia toDomain(IncidenciaJpaEntity entity);
    IncidenciaJpaEntity toJpaEntity(Incidencia domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Incidencia toDomain(IncidenciaRequest request);

    IncidenciaResponse toResponse(Incidencia domain);
}
