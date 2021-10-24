package pl.edu.pg.eti.kask.forge.equipment.controller;

import pl.edu.pg.eti.kask.forge.equipment.dto.*;
import pl.edu.pg.eti.kask.forge.equipment.entity.Equipment;
import pl.edu.pg.eti.kask.forge.equipment.service.EquipmentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.Optional;

@Path("")
public class EquipmentController {

    private EquipmentService service;

    public EquipmentController(){
    }

    @Inject
    public void setService(EquipmentService service){
        this.service = service;
    }

    @GET
    @Path("/equipments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(){
        return Response
                .ok(GetEquipmentsResponse.entityToDtoMapper().apply(service.findAll()))
                .build();
    }

    @GET
    @Path("/equipments/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEquipment(@PathParam("id") Long id){
        Optional<Equipment> equipment = service.find(id);
        if(equipment.isPresent()){
            return Response
                    .ok(GetEquipmentResponse.entityToDtoMapper().apply(equipment.get()))
                    .build();
        }
        else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @POST
    @Path("/equipments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postEquipment(CreateEquipmentRequest request){
        Equipment equipment = CreateEquipmentRequest.dtoToEntityMapper().apply(request);
        service.create(equipment);
        return Response
                .created(UriBuilder
                        .fromMethod(EquipmentController.class, "getEquipment")
                        .build(equipment.getId()))
                .build();
    }

    @PUT
    @Path("/equipments/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putEquipment(@PathParam("id") Long id, UpdateEquipmentRequest request){
        Optional<Equipment> equipment = service.find(id);
        if(equipment.isPresent()){
            UpdateEquipmentRequest.dtoToEntityUpdater().apply(equipment.get(), request);

            service.update(equipment.get());
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

    @PUT
    @Path("/equipments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putEquipments(UpdateEquipmentsRequest request){
        return Response
                .status(Response.Status.NOT_IMPLEMENTED)
                .build();
    }

    @DELETE
    @Path("/equipments/{id}")
    public Response deleteEquipment(@PathParam("id") Long id){
        Optional<Equipment> equipment = service.find(id);
        if(equipment.isPresent()){
            service.delete(id);
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
    @Path("/equipments")
    public Response deleteEquipments() {
        service.deleteAll();

        return Response
                .ok()
                .build();
    }
}
