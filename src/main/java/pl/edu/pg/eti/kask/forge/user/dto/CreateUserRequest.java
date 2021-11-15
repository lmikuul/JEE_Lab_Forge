package pl.edu.pg.eti.kask.forge.user.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.errand.dto.CreateErrandRequest;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateUserRequest {
    /**
     * User's login
     */
    private String login;
    /**
     * User's password
     */
    private String password;
    /**
     * User's birthdate
     */
    private LocalDate birthDate;
    /**
     * User's role in system defining privileges
     */
    private List<String> roles;

    public static Function<CreateUserRequest, User> dtoToEntityMapper(){
        return request -> User.builder()
                .login(request.getLogin())
                .password(request.getPassword())
                .birthDate(request.getBirthDate())
                .roles(request.getRoles())
                .build();
    }
}
