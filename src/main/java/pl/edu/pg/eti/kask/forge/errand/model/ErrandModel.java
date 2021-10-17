package pl.edu.pg.eti.kask.forge.errand.model;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentModel;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentsModel;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.model.UserModel;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ErrandModel {
    private long id;
    /**
     * Errand's user
     */
    private UserModel user;
    /**
     * Errand's equipment
     */
    private EquipmentModel equipment;
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

    public static Function<Errand, ErrandModel> entityToModelMapper() {
        return errand -> ErrandModel.builder()
                .id(errand.getId())
                .price(errand.getPrice())
                .startDay(errand.getStartDay())
                .doneDay(errand.getDoneDay())
                .details(errand.getDetails())
                .user(UserModel.entityToModelMapper().apply(errand.getUser()))
                .equipment(EquipmentModel.entityToModelMapper().apply(errand.getEquipment()))
                .build();
    }
}
