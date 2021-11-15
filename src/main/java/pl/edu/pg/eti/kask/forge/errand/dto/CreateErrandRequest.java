package pl.edu.pg.eti.kask.forge.errand.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Supplier;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateErrandRequest {
    /**
     * Errand's id
     */
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

    public static Function<CreateErrandRequest, Errand> dtoToEntityMapper(
            Supplier<Equipment> equipmentSupplier
    ){
        return request -> Errand.builder()
                .id(request.getId())
                .equipment(equipmentSupplier.get())
                .price(request.getPrice())
                .startDay(request.getStartDay())
                .doneDay(request.getDoneDay())
                .details(request.getDetails())
                .build();
    }
}
