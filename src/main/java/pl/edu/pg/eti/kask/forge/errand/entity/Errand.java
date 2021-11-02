package pl.edu.pg.eti.kask.forge.errand.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.user.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "errands")
public class Errand implements Serializable {
    /**
     * Errand's id
     */
    @Id
    private long id;
    /**
     * Errand's user
     */
    @ManyToOne
    @JoinColumn(name ="user")
    private User user;
    /**
     * Errand's equipment
     */
    @ManyToOne
    @JoinColumn(name ="equipment")
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
}
