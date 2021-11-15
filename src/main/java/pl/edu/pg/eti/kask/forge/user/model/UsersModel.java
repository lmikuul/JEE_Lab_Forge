package pl.edu.pg.eti.kask.forge.user.model;

import lombok.*;
import pl.edu.pg.eti.kask.forge.user.entity.Role;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents list of characters to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UsersModel  implements Serializable {
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
    public static class ModelUser {

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
        private List<String> roles;

    }

    /**
     * Name of the selected characters.
     */
    @Singular
    private List<ModelUser> modelUsers;

    /**
     * @return mapper for convenient converting entity object to model object
     */
    public static Function<Collection<pl.edu.pg.eti.kask.forge.user.entity.User>, UsersModel> entityToModelMapper() {
        return characters -> {
            UsersModel.UsersModelBuilder model = UsersModel.builder();
            characters.stream()
                    .map(user -> ModelUser.builder()
                            .login(user.getLogin())
                            .birthDate(user.getBirthDate())
                            .roles(user.getRoles())
                            .build())
                    .forEach(model::modelUser);
            return model.build();
        };
    }
}
