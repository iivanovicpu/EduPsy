package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsJson;

import java.util.HashMap;

import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 05.10.16.
 */
public class ProfileController extends AbstractController {

    public static Route fetchPersonalProfile = (Request request, Response response)-> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("validation", false);
            return ViewUtil.render(request, model, Path.Template.PERSONAL_PROFILE);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(user);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitPersonalProfile = (Request request, Response response)-> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        String color = request.queryParams("color");
        user.setColor("main_" + color + ".css");
        dbProvider.save(user);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("validation", false);
            return ViewUtil.render(request, model, Path.Template.PERSONAL_PROFILE);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(user);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
