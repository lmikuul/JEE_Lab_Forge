package pl.edu.pg.eti.kask.forge.errand.controller;

import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.service.EquipmentService;
import pl.edu.pg.eti.kask.forge.errand.dto.CreateErrandRequest;
import pl.edu.pg.eti.kask.forge.errand.dto.GetErrandResponse;
import pl.edu.pg.eti.kask.forge.errand.dto.GetErrandsResponse;
import pl.edu.pg.eti.kask.forge.errand.dto.UpdateErrandRequest;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.service.ErrandService;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.Optional;

@Path("")
public class ErrandController {

    private ErrandService errandService;
    private UserService userService;
    private EquipmentService equipmentService;

    public ErrandController(){
    }

    @Inject
    public void setErrandService(ErrandService service){
        this.errandService = service;
    }

    @Inject
    public void setUserService(UserService service){this.userService = service;}

    @Inject
    public void setEquipmentService(EquipmentService service){
        this.equipmentService = service;
    }

    @GET
    @Path("/equipments/{eqId}/errands")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getErrands(@PathParam("eqId") Long eqId){
        Optional<List<Errand>> errands = errandService.findAllForEquipment(eqId);
        if(errands.isEmpty()){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        else{
            return Response
                    .ok(GetErrandsResponse.entityToDtoMapper().apply(errands.get()))
                    .build();
        }
    }

    @GET
    @Path("/equipments/{eqId}/errands/{errandId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getErrand(@PathParam("eqId") Long eqId, @PathParam("errandId") Long errandId){
        Optional<Errand> errand = errandService.findForEquipment(eqId, errandId);
        if(errand.isPresent()){
            return Response
                    .ok(GetErrandResponse.entityToDtoMapper().apply(errand.get()))
                    .build();
        }
        else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @POST
    @Path("/equipments/{eqId}/errands/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postErrand(CreateErrandRequest request, @PathParam("eqId") Long eqId){
        Errand errand = CreateErrandRequest
                .dtoToEntityMapper(login -> userService.find(login).orElse(null),
                                    () -> equipmentService.find(eqId).orElse(null))
                .apply(request);

        errandService.create(errand);
        return Response
                .created(UriBuilder
                        .fromMethod(ErrandController.class, "getErrand")
                        .build(eqId, errand.getId()))
                .build();
    }

    @PUT
    @Path("/equipments/{eqId}/errands/{errandId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putErrand(@PathParam("eqId") Long eqId, @PathParam("errandId") Long errandId, UpdateErrandRequest request){
        Optional<Errand> errand = errandService.find(errandId);
        if(errand.isPresent()){
            UpdateErrandRequest.dtoToEntityUpdater(login -> userService.find(login).orElse(null),
                            () -> equipmentService.find(eqId).orElse(null))
                    .apply(errand.get(), request);

            errandService.update(errand.get());
            return Response
                    .ok()
                    .build();

        }
        else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @DELETE
    @Path("/equipments/{eqId}/errands/{errandId}")
    public Response deleteErrand(@PathParam("eqId") Long eqId, @PathParam("errandId") Long errandId){
        Optional<Errand> errand = errandService.findForEquipment(eqId, errandId);
        if(errand.isPresent()){
            errandService.delete(errand.get().getId());
            return Response
                    .ok()
                    .build();
        }
        else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @DELETE
    @Path("/equipments/{eqId}/errands")
    public Response deleteErrands(@PathParam("eqId") Long eqId) {
        errandService.deleteAllForEquipment(eqId);

        return Response
                .ok()
                .build();
    }
}
