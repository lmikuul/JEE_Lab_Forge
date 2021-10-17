package pl.edu.pg.eti.kask.forge.equipment.model.converter;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentModel;
import pl.edu.pg.eti.kask.forge.equipment.service.EquipmentService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Faces converter for {@link EquipmentModel}. The managed attribute in {@link @FacesConverter} allows the converter
 * to be the CDI bean. In previous version of JSF converters were always created inside JSF lifecycle and where not
 * managed by container that is injection was not possible. As this bean is not annotated with scope the beans.xml
 * descriptor must be present.
 */
@FacesConverter(forClass = EquipmentModel.class, managed = true)
public class EquipmentModelConverter implements Converter<EquipmentModel> {
    /**
     * Service for professions management.
     */
    private final EquipmentService service;

    @Inject
    public EquipmentModelConverter(EquipmentService service) {
        this.service = service;
    }

    @Override
    public EquipmentModel getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        Optional<Equipment> equipment = service.find(value);
        return equipment.isEmpty() ? null : EquipmentModel.entityToModelMapper().apply(equipment.get());
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, EquipmentModel equipmentModel) {
        return equipmentModel == null ? "" : equipmentModel.getName();
    }
}
