package dam.mod.centroplus.adapters.mapper;

import dam.mod.centroplus.adapters.in.api.ReservaRequest;
import dam.mod.centroplus.adapters.in.api.ReservaResponse;
import dam.mod.centroplus.adapters.out.persistence.ReservaJpaEntity;
import dam.mod.centroplus.domain.model.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(target = "nombreActividad", ignore = true)
    Reserva toDomain(ReservaJpaEntity entity);

    ReservaJpaEntity toJpaEntity(Reserva domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "nombreActividad", ignore = true)
    Reserva toDomain(ReservaRequest request);

    ReservaResponse toResponse(Reserva domain);
}
