package pl.edu.pg.eti.kask.forge.errand.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.user.entity.User;

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
public class UpdateErrandRequest {
    /**
     * Errand's user
     */
    private String user;
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

    public static BiFunction<Errand, UpdateErrandRequest, Errand>dtoToEntityUpdater(
            Function<String, User> userFunction,
            Supplier<Equipment> equipmentSupplier){
        return (errand, request) -> {
            errand.setUser(userFunction.apply(request.getUser()));
            errand.setEquipment(equipmentSupplier.get());
            errand.setPrice(request.getPrice());
            errand.setStartDay(request.getStartDay());
            errand.setDoneDay(request.getDoneDay());
            errand.setDetails(request.getDetails());

            return errand;
        };
    }
}
