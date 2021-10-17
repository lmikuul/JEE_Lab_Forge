package pl.edu.pg.eti.kask.forge.equipment.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Equipment implements Serializable {
    /**
     * Equipment's id
     */
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
}
