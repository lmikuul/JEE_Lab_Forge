package pl.edu.pg.eti.kask.forge.user.servlet;

import pl.edu.pg.eti.kask.forge.servlet.MimeTypes;
import pl.edu.pg.eti.kask.forge.servlet.ServletUtility;
import pl.edu.pg.eti.kask.forge.user.entity.User;
import pl.edu.pg.eti.kask.forge.user.service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Servlet for serving and uploading user's avatar i raster image format.
 */
@WebServlet(
        urlPatterns = {AvatarServlet.Paths.AVATAR + "/*"},
        initParams = {@WebInitParam(name = "avatarsPath", value = "E:\\JEE\\avatars")}
)
@MultipartConfig(maxFileSize = 200 * 1024)
public class AvatarServlet extends HttpServlet {

    /**
     * Service for managing users.
     */
    private final UserService service;

    @Inject
    public AvatarServlet(UserService service) {
        this.service = service;
    }

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * Specified avatar for download and upload.
         */
        public static final String AVATAR = "/api/avatars";

    }

    /**
     * Definition of regular expression patterns supported by this servlet. Separate inner class provides composition
     * for static fields. Whereas servlet activation path can be compared to {@link Paths} the path info (denoted by
     * wildcard in paths) can be compared using regular expressions.
     */
    public static class Patterns {

        /**
         * Specified avatar (for download).
         */
        public static final String AVATAR = "^/[A-Za-z0-9]+/?$";

    }

    /**
     * Request parameters (both query params and request parts) which can be sent by the client.
     */
    public static class Parameters {

        /**
         * Avatar image part.
         */
        public static final String AVATAR = "avatar";

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.AVATAR.equals(servletPath)) {
            if (path.matches(Patterns.AVATAR)) {
                getAvatar(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();

        if (Paths.AVATAR.equals(servletPath)) {
            if (path.matches(Patterns.AVATAR)) {
                putAvatar(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.AVATAR.equals(servletPath)) {
            if (path.matches(Patterns.AVATAR)) {
                deleteAvatar(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Deletes user's avatar
     *
     * @param request  http request
     * @param response http response
     * @throws IOException      if any input or output exception occurred
     */
    private void deleteAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String login = ServletUtility.parseRequestPath(request).replaceAll("/", "");
        Optional<User> user = service.find(login);

        if (user.isPresent()) {
            String path = getInitParameter("avatarsPath")+"/"+login+".jpg";

            service.deleteAvatar(path);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Updates user's avatar. Receives avatar bytes from request and stores them in the data storage.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException      if any input or output exception occurred
     * @throws ServletException if this request is not of type multipart/form-data
     */
    private void putAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Parsed request path is valid with user pattern and can contain starting and ending '/'.
        String login = ServletUtility.parseRequestPath(request).replaceAll("/", "");
        Optional<User> user = service.find(login);

        if(user.isPresent()){
            InputStream avatar = request.getPart(Parameters.AVATAR).getInputStream();
            String path = getInitParameter("avatarsPath")+"/"+login+".jpg";

            try {
                service.uploadAvatar(avatar, path);
            }
            catch (IOException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else{
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Fetches avatar as byte array from data storage and sends is through http protocol.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = ServletUtility.parseRequestPath(request).replaceAll("/", "");
        Optional<User> user = service.find(login);

        if (user.isPresent()) {
            String path = getInitParameter("avatarsPath")+"/"+login+".jpg";
            byte[] file = service.findAvatar(path);

            if(file != null){
                response.addHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.IMAGE_JPG);
                response.setContentLength(file.length);
                response.getOutputStream().write(file);
            }
            else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
