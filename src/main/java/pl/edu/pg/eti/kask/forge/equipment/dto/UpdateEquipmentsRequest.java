package pl.edu.pg.eti.kask.forge.equipment.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.entity.EquipmentType;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateEquipmentsRequest {

    @Singular
    private List<Equipment> equipments;

    public static BiFunction<Collection<Equipment>, UpdateEquipmentsRequest, List<Equipment>> dtoToEntityUpdater() {
        return (equipment, request) -> request.getEquipments();
    }

}
