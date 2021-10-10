package pl.edu.pg.eti.kask.forge.user.dto;

import lombok.*;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * GET user response. Contains only field's which can be displayed on frontend. User is defined in
 * {@link pl.edu.pg.eti.kask.forge.user.entity.User}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUserResponse {

    /**
     * User's login
     */
    private String login;
    /**
     * User's role in system defining privileges
     */
    private Role role;
    /**
     * User's birthdate.
     */
    private LocalDate birthDate;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<User, GetUserResponse> entityToDtoMapper() {
        return user -> GetUserResponse.builder()
                .login(user.getLogin())
                .birthDate(user.getBirthDate())
                .role(user.getRole())
                .build();
    }

}