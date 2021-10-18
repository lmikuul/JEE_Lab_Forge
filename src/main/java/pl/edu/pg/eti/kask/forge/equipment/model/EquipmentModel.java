package pl.edu.pg.eti.kask.forge.equipment.model;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.entity.EquipmentType;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.model.UserEditModel;
import pl.edu.pg.eti.kask.forge.user.model.UserModel;

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
public class EquipmentModel {
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

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Equipment, EquipmentModel> entityToModelMapper() {
        return equipment -> EquipmentModel.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .type(equipment.getType())
                .mainMaterial(equipment.getMainMaterial())
                .weight(equipment.getWeight())
                .build();
    }


    /**
     * @return updater for convenient updating entity object using model object
     */
    public static BiFunction<Equipment, EquipmentModel, Equipment> modelToEntityMapper() {
        return (equipment, request) -> {
            equipment.setId(equipment.getId());
            equipment.setName(equipment.getName());
            equipment.setType(equipment.getType());
            equipment.setWeight(equipment.getWeight());
            equipment.setMainMaterial(equipment.getMainMaterial());

            return equipment;
        };
    }
}
