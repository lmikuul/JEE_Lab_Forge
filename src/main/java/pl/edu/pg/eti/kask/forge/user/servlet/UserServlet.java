package pl.edu.pg.eti.kask.forge.user.servlet;

import pl.edu.pg.eti.kask.forge.servlet.MimeTypes;
import pl.edu.pg.eti.kask.forge.servlet.ServletUtility;
import pl.edu.pg.eti.kask.forge.user.dto.GetUserResponse;
import pl.edu.pg.eti.kask.forge.user.dto.GetUsersResponse;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for returning user's name from the active session if present.
 */
@WebServlet(urlPatterns = UserServlet.Paths.USERS + "/*")
public class UserServlet extends HttpServlet {
    /**
     * Service for user entity operations.
     */
    private final UserService service;

    @Inject
    public UserServlet(UserService service) {
        this.service = service;
    }

    public static class Paths {

        /**
         * All users
         */
        public static final String USERS = "/api/users";
    }

    public static class Patterns {

        /**
         * All users
         */
        public static final String ALLUSERS = "^/?$";

        /**
         * Specified user
         */
        public static final String ONEUSER = "^/[A-Za-z0-9]+/?$";

    }

    /**
     * JSON-B mapping object. According to open liberty documentation creating those is expensive. The JSON-B is only
     * one of many solutions. JSON strings can be build by hand {@link StringBuilder} or with JSON-P API. Both JSON-B
     * and JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ServletUtility.parseRequestPath(request);

        if (path.matches(Patterns.ONEUSER)) {
            getUser(request, response);
            return;
        }
        else if (path.matches(Patterns.ALLUSERS)) {
            getUsers(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Sends single user or 404 error if not found.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Parsed request path is valid with character pattern and can contain starting and ending '/'.
        String login = ServletUtility.parseRequestPath(request).replaceAll("/", "");
        Optional<User> user = service.find(login);

        if (user.isPresent()) {
            response.setContentType(MimeTypes.APPLICATION_JSON);
            response.getWriter()
                    .write(jsonb.toJson(GetUserResponse.entityToDtoMapper().apply(user.get())));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Sends all characters as JSON.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(MimeTypes.APPLICATION_JSON);
        response.getWriter()
                .write(jsonb.toJson(GetUsersResponse.entityToDtoMapper().apply(service.findAll())));
    }

}

