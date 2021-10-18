package pl.edu.pg.eti.kask.forge.errand.model;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentModel;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.view.ErrandCreate;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.model.UserModel;

import java.time.LocalDate;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ErrandCreateModel {
    private long id;

    private EquipmentModel equipment;

    private UserModel user;
    /**
     * Errand's price
     */
    private double price;
    /**
     * Errand's startDay
     */
    private LocalDate startDay;
    /**
     * Errand's doneDay
     */
    private LocalDate doneDay;
    /**
     * Errand's details
     */
    private String details;

    /**
     * @param equipmentFunction function for converting profession name to profession entity object
     * @param userFunction       supplier for providing new character owner
     * @return mapper for convenient converting model object to entity object
     */
    public static Function<ErrandCreateModel, Errand> modelToEntityMapper(
            Function<Long, Equipment> equipmentFunction,
            Function<String, User> userFunction) {

        return model -> Errand.builder()
                .id(model.getId())
                .price(model.getPrice())
                .startDay(model.getStartDay())
                .doneDay(model.getDoneDay())
                .details(model.getDetails())
                .equipment(equipmentFunction.apply(model.getEquipment().getId()))
                .user(userFunction.apply(model.getUser().getLogin()))
                .build();
    }
}
