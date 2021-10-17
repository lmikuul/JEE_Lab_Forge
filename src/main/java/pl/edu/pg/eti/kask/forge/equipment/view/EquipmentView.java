package pl.edu.pg.eti.kask.forge.equipment.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.model.EquipmentModel;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequestScoped
@Named
public class EquipmentView {
    /**
     * Service for managing characters.
     */
    private final EquipmentService equipmentService;
    /**
     * Equipment's id
     */
    @Getter
    @Setter
    private long id;

    @Getter
    private EquipmentModel equipment;

    @Inject
    public EquipmentView(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }
    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached wihitn
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<Equipment> equipment = equipmentService.find(id);
        if (equipment.isPresent()) {
            this.equipment = EquipmentModel.entityToModelMapper().apply(equipment.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Equipment not found");
        }
    }
}
