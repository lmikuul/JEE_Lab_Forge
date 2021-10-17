package pl.edu.pg.eti.kask.forge.errand.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * GET users response. Contains logins of users in the system.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetErrandsResponse {
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
    public static class dtoGetErrand {
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
         * Errand's details
         */
        private String details;
    }

    @Singular
    private List<dtoGetErrand> dtoGetErrands;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<pl.edu.pg.eti.kask.forge.errand.entity.Errand>, GetErrandsResponse> entityToDtoMapper() {
        return errands -> {
            GetErrandsResponse.GetErrandsResponseBuilder response = GetErrandsResponse.builder();
            errands.stream()
                    .map(errand -> dtoGetErrand.builder()
                            .id(errand.getId())
                            .user(errand.getUser())
                            .equipment(errand.getEquipment())
                            .price(errand.getPrice())
                            .details(errand.getDetails())
                            .build())
                    .forEach(response::dtoGetErrand);
            return response.build();
        };
    }

}
