package pl.edu.pg.eti.kask.forge.equipment.view;

import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentsModel;
import pl.edu.pg.eti.kask.forge.equipment.service.EquipmentService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@RequestScoped
@Named
public class EquipmentList implements Serializable {

    /**
     * Service for managing characters.
     */
    private final EquipmentService service;

    /**
     * Characters list exposed to the view.
     */
    private EquipmentsModel equipments;

    @Inject
    public EquipmentList(EquipmentService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached using
     * lazy getter.
     *
     * @return all characters
     */
    public EquipmentsModel getEquipments() {
        if (equipments == null) {
            equipments = EquipmentsModel.entityToModelMapper().apply(service.findAll());
        }
        return equipments;
    }

    public String deleteAction(EquipmentsModel.ModelEquipment equipment) {
        service.delete(equipment.getId());
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true";
    }
}
