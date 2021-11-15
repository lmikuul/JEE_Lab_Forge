package pl.edu.pg.eti.kask.forge.user.controller;

import pl.edu.pg.eti.kask.forge.user.dto.CreateUserRequest;
import pl.edu.pg.eti.kask.forge.user.dto.GetUserResponse;
import pl.edu.pg.eti.kask.forge.user.dto.GetUsersResponse;
import pl.edu.pg.eti.kask.forge.user.dto.UpdateUserRequest;
import pl.edu.pg.eti.kask.forge.user.entity.Role;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Optional;

@Path("")
@RolesAllowed(Role.USER)
public class UserController {

    private UserService userService;

    public UserController(){}

    @EJB
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(){
        return Response
                .ok(GetUsersResponse.entityToDtoMapper().apply(userService.findAll()))
                .build();
    }

    @GET
    @Path("/users/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("login") String login){
        Optional<User> user = userService.find(login);
        if(user.isPresent()){
            return Response
                    .ok(GetUserResponse.entityToDtoMapper().apply(user.get()))
                    .build();
        }
        else {
            return Response
                    .ok(new Object())
                    .build();
        }
    }

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        Optional<User> user = userService.findCallerPrincipal();
        if (user.isPresent()) {
            return Response
                    .ok(GetUserResponse.entityToDtoMapper().apply(user.get()))
                    .build();
        } else {
            return Response
                    .ok(new Object())
                    .build();
        }
    }

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response postUser(CreateUserRequest request){
        User user = CreateUserRequest.dtoToEntityMapper().apply(request);
        userService.create(user);
        return Response
                .created(UriBuilder
                        .fromPath("/users/{login}")
                        .build(user.getLogin()))
                .build();
    }


    @PUT
    @Path("/users/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUser(UpdateUserRequest request, @PathParam("login") String login){
        Optional<User> user = userService.find(login);
        if(user.isPresent()){
            UpdateUserRequest.dtoToEntityUpdater().apply(user.get(), request);

            userService.update(user.get());
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
    @Path("/users/{login}")
    @RolesAllowed(Role.SYSTEM_ADMIN)
    public Response deleteUser(@PathParam("login") String login){
        Optional<User> user = userService.find(login);
        if(user.isPresent()){
            userService.delete(user.get());
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

}
