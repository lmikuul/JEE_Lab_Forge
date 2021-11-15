package pl.edu.pg.eti.kask.forge.user.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.dto.UpdateEquipmentRequest;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import java.time.LocalDate;
import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateUserRequest {

    /**
     * User's password
     */
    private String password;

    public static BiFunction<User,UpdateUserRequest, User> dtoToEntityUpdater() {
        return (user, request) -> {
            user.setPassword(request.getPassword());

            return user;
        };
    }
}
