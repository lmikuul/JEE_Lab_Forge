package pl.edu.pg.eti.kask.forge.equipment.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentsModel;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetEquipmentsResponse {

    @Singular
    private List<String> equipments;

    public static Function<Collection<Equipment>, GetEquipmentsResponse> entityToDtoMapper() {
        return equipments -> {
            GetEquipmentsResponse.GetEquipmentsResponseBuilder response = GetEquipmentsResponse.builder();
            equipments.stream()
                    .map(Equipment::getName)
                    .forEach(response::equipment);
            return response.build();
        };
    }

}
