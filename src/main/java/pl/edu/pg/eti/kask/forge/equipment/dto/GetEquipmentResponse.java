package pl.edu.pg.eti.kask.forge.equipment.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.entity.EquipmentType;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetEquipmentResponse {
    /**
     * Equipment's id
     */
    private long id;
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

    public static Function<Equipment, GetEquipmentResponse>entityToDtoMapper(){
        return equipment -> GetEquipmentResponse.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .type(equipment.getType())
                .mainMaterial(equipment.getMainMaterial())
                .weight(equipment.getWeight())
                .build();
    }
}
