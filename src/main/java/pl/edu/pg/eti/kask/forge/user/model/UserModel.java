package pl.edu.pg.eti.kask.forge.user.model;

import lombok.*;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents single character to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UserModel {
    /**
     * User's login
     */
    private String login;
    /**
     * User's birthdate
     */
    private LocalDate birthDate;
    /**
     * User's role in system defining privileges
     */
    private Role role;
    /**
     * User's role in system defining privileges
     */
    private String avatar;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<User, UserModel> entityToModelMapper() {
        return user -> UserModel.builder()
                .login(user.getLogin())
                .birthDate(user.getBirthDate())
                .role(user.getRole())
                .avatar("/api/avatars/" + user.getLogin())
                .build();
    }
}
