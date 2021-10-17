package pl.edu.pg.eti.kask.forge.errand.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentsModel;
import pl.edu.pg.eti.kask.forge.equipment.service.EquipmentService;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.model.ErrandsModel;
import pl.edu.pg.eti.kask.forge.errand.service.ErrandService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named
public class ErrandListByEquipment implements Serializable {

    /**
     * Service for managing characters.
     */
    private final ErrandService service;

    @Setter
    @Getter
    private Long equipmentId;
    /**
     * Characters list exposed to the view.
     */
    private ErrandsModel errands;

    @Inject
    public ErrandListByEquipment(ErrandService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached using
     * lazy getter.
     *
     * @return all characters
     */
    public ErrandsModel getErrands() {
        if (errands == null) {
            errands = ErrandsModel.entityToModelMapper().apply(service.findAllForEquipment(equipmentId));
        }
        return errands;
    }

    public String deleteAction(ErrandsModel.ModelErrand errand) {
        service.delete(errand.getId());
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?id="+equipmentId+"&faces-redirect=true";
    }
}
