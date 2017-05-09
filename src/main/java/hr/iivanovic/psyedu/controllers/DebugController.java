package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;

import java.util.HashMap;
import java.util.Objects;

import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 08.05.17.
 */
public class DebugController {
    public static Route submitDebugRules = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {

        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

}
