package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.RequestUtil.getQueryLoginRedirect;
import static hr.iivanovic.psyedu.util.RequestUtil.getQueryPassword;
import static hr.iivanovic.psyedu.util.RequestUtil.getQueryUsername;
import static hr.iivanovic.psyedu.util.RequestUtil.removeSessionAttrLoggedOut;
import static hr.iivanovic.psyedu.util.RequestUtil.removeSessionAttrLoginRedirect;

import java.util.HashMap;
import java.util.Map;

import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginController {

    public static final String CURRENT_USER = "currentUser";

    public static Route serveLoginPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.render(request, model, Path.Template.LOGIN);
    };

    public static Route handleLoginPost = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        User user = UserController.authenticate(getQueryUsername(request), getQueryPassword(request));
        if (null == user) {
            model.put("authenticationFailed", true);
            return ViewUtil.render(request, model, Path.Template.LOGIN);
        }
        model.put("authenticationSucceeded", true);
        request.session().attribute("currentUser", user);
        if (getQueryLoginRedirect(request) != null) {
            response.redirect(getQueryLoginRedirect(request));
        }
        return ViewUtil.render(request, model, Path.Template.LOGIN);
    };

    public static Route handleLogoutPost = (Request request, Response response) -> {
        request.session().removeAttribute(CURRENT_USER);
        request.session().attribute("loggedOut", true);
        response.redirect(Path.Web.LOGIN);
        return null;
    };

    // The origin of the request (request.pathInfo()) is saved in the session so
    // the user can be redirected back after login
    public static void ensureUserIsLoggedIn(Request request, Response response) {
        if (request.session().attribute(CURRENT_USER) == null) {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect(Path.Web.LOGIN);
        }
    };

    public static boolean isEditAllowed(Request request){
        User currentUser = request.session().attribute(CURRENT_USER);
        return currentUser != null && ("TEACHER".equals(currentUser.getStatus()) || "ADMIN".equals(currentUser.getStatus()));
    }

    public static User getCurrentUser(Request request){
        return request.session().attribute(CURRENT_USER);
    }

}
