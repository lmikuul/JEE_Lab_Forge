package pl.edu.pg.eti.kask.forge.errand.servlet;

import pl.edu.pg.eti.kask.forge.errand.dto.GetErrandsResponse;
import pl.edu.pg.eti.kask.forge.errand.entity.Errand;
import pl.edu.pg.eti.kask.forge.errand.service.ErrandService;
import pl.edu.pg.eti.kask.forge.servlet.MimeTypes;
import pl.edu.pg.eti.kask.forge.servlet.ServletUtility;
import pl.edu.pg.eti.kask.forge.user.dto.GetUserResponse;
import pl.edu.pg.eti.kask.forge.user.dto.GetUsersResponse;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.service.UserService;
import pl.edu.pg.eti.kask.forge.user.servlet.UserServlet;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = ErrandServlet.Paths.ERRANDS + "/*")
public class ErrandServlet extends HttpServlet {
    /**
     * Service for user entity operations.
     */
    private final ErrandService service;

    @Inject
    public ErrandServlet(ErrandService service) {
        this.service = service;
    }

    public static class Paths {

        /**
         * All errands
         */
        public static final String ERRANDS = "/api/errands";
    }

    public static class Patterns {

        /**
         * All users
         */
        public static final String ALLERRANDS = "^/?$";

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

        if (path.matches(Patterns.ALLERRANDS)) {
            getErrands(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Sends all characters as JSON.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void getErrands(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(MimeTypes.APPLICATION_JSON);
        response.getWriter()
                .write(jsonb.toJson(GetErrandsResponse.entityToDtoMapper().apply(service.findAll())));
    }
}
