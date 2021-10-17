package pl.edu.pg.eti.kask.forge.errand.model;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.entity.EquipmentType;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentsModel;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import java.io.Serializable;
import java.time.LocalDate;
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
public class ErrandsModel implements Serializable {
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
    public static class ModelErrand {
        /**
         * Errand's id
         */
        private long id;
        /**
         * Errand's user login
         */
        private String userLogin;
        /**
         * Errand's startDay
         */
        private LocalDate startDay;
    }

    /**
     * Name of the selected characters.
     */
    @Singular
    private List<ErrandsModel.ModelErrand> modelErrands;

    /**
     * @return mapper for convenient converting entity object to model object
     */
    public static Function<Collection<pl.edu.pg.eti.kask.forge.errand.entity.Errand>, ErrandsModel> entityToModelMapper() {
        return errands -> {
            ErrandsModel.ErrandsModelBuilder model = ErrandsModel.builder();
            errands.stream()
                    .map(errand -> ModelErrand.builder()
                            .id(errand.getId())
                            .userLogin(errand.getUser().getLogin())
                            .startDay(errand.getStartDay())
                            .build())
                    .forEach(model::modelErrand);
            return model.build();
        };
    }
}
