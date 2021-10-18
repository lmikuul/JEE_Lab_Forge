package pl.edu.pg.eti.kask.forge.errand.model;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentModel;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.model.UserEditModel;
import pl.edu.pg.eti.kask.forge.user.model.UserModel;

import java.time.LocalDate;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ErrandEditModel {
    private long id;
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

    public static Function<Errand, ErrandEditModel> entityToModelMapper() {
        return errand -> ErrandEditModel.builder()
                .id(errand.getId())
                .price(errand.getPrice())
                .startDay(errand.getStartDay())
                .doneDay(errand.getDoneDay())
                .details(errand.getDetails())
                .build();
    }

    /**
     * @return updater for convenient updating entity object using model object
     */
    public static BiFunction<Errand, ErrandEditModel, Errand> modelToEntityUpdater() {
        return (errand, request) -> {
            errand.setPrice(request.getPrice());
            errand.setStartDay(request.getStartDay());
            errand.setDoneDay(request.getDoneDay());
            errand.setDetails(request.getDetails());

            return errand;
        };
    }
}
