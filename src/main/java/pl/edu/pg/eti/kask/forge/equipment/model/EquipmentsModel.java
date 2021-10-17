package pl.edu.pg.eti.kask.forge.equipment.model;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.EquipmentType;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents list of characters to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class EquipmentsModel implements Serializable {
    /**
     * Represents single character in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class ModelEquipment {
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
    }

    /**
     * Name of the selected characters.
     */
    @Singular
    private List<ModelEquipment> modelEquipments;

    /**
     * @return mapper for convenient converting entity object to model object
     */
    public static Function<Collection<pl.edu.pg.eti.kask.forge.equipment.entity.Equipment>, EquipmentsModel> entityToModelMapper() {
        return equipments -> {
            EquipmentsModel.EquipmentsModelBuilder model = EquipmentsModel.builder();
            equipments.stream()
                    .map(equipment -> ModelEquipment.builder()
                            .id(equipment.getId())
                            .name(equipment.getName())
                            .type(equipment.getType())
                            .build())
                    .forEach(model::modelEquipment);
            return model.build();
        };
    }
}
