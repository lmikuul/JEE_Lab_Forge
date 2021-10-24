package pl.edu.pg.eti.kask.forge.errand.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetErrandResponse {
    /**
     * Errand's id
     */
    private long id;
    /**
     * Errand's user
     */
    private User user;
    /**
     * Errand's equipment
     */
    private Equipment equipment;
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

    public static Function<Errand, GetErrandResponse>entityToDtoMapper(){
        return errand -> GetErrandResponse.builder()
                .id(errand.getId())
                .user(errand.getUser())
                .equipment(errand.getEquipment())
                .price(errand.getPrice())
                .startDay(errand.getStartDay())
                .doneDay(errand.getDoneDay())
                .details(errand.getDetails())
                .build();
    }
}
