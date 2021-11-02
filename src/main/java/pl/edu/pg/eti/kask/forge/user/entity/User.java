package pl.edu.pg.eti.kask.forge.user.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User implements Serializable {
    /**
     * User's login
     */
    @Id
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
    private Role role;

    @ToString.Exclude//It's common to exclude lists from toString
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Errand> errands;
}
