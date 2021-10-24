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
public class CreateEquipmentRequest {
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

    public static Function<CreateEquipmentRequest, Equipment> dtoToEntityMapper(){
        return request -> Equipment.builder()
                .id(request.getId())
                .name(request.getName())
                .type(request.getType())
                .mainMaterial(request.getMainMaterial())
                .weight(request.getWeight())
                .build();
    }
}
