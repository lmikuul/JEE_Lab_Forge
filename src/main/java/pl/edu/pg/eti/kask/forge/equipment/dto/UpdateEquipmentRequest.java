package pl.edu.pg.eti.kask.forge.equipment.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.entity.EquipmentType;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentModel;

import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateEquipmentRequest {
    /**
     * Equipment's name
     */
    private String name;
    /**
     * Equipment's type
     */
    private EquipmentType type;
    /**
     * Equipment's material
     */
    private String mainMaterial;
    /**
     * Equipment's weight
     */
    private double weight;


    public static BiFunction<Equipment, UpdateEquipmentRequest, Equipment> dtoToEntityUpdater() {
        return (equipment, request) -> {
            equipment.setName(request.getName());
            equipment.setType(request.getType());
            equipment.setWeight(request.getWeight());
            equipment.setMainMaterial(request.getMainMaterial());
            return equipment;
        };
    }
}
