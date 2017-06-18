package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsJson;

import java.util.HashMap;

import hr.iivanovic.psyedu.db.IntelligenceType;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 05.10.16.
 */
public class ProfileController extends AbstractController {
    private final static String PERSONAL_PROFILE = "/velocity/profile.vm";

    public static Route fetchPersonalProfile = (Request request, Response response)-> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            return createProfileModel(request, user);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(user);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static Object createProfileModel(Request request, User user) {
        HashMap<String, Object> model = new HashMap<>();
        model.put("validation", false);
        model.put("intelligenceType", IntelligenceType.getById(user.getIntelligenceTypeId()).getDescription());
        return ViewUtil.render(request, model, PERSONAL_PROFILE);
    }

    public static Route submitPersonalProfile = (Request request, Response response)-> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        String color = request.queryParams("color");
        user.setColor("main_" + color + ".css");
        dbProvider.save(user);
        if (clientAcceptsHtml(request)) {
            return createProfileModel(request, user);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(user);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
