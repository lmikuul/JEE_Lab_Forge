package pl.edu.pg.eti.kask.forge.equipment.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;

import javax.persistence.*;
import javax.ws.rs.Path;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "equipments")
public class Equipment implements Serializable {
    /**
     * Equipment's id
     */
    @Id
    private long id;
    /**
     * Equipment's name
     */
    private String name;
    /**
     * Equipment's type
     */
    private EquipmentType type;
    /**
     * Equipment's material
     */
    private String mainMaterial;
    /**
     * Equipment's weight
     */
    private double weight;

    @ToString.Exclude//It's common to exclude lists from toString
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Errand> errands;
}
