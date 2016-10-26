package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;

import java.util.HashMap;

import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 26.10.16.
 */
public class PollController extends AbstractController {

    public static Route showPollIntelligenceType = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("validation", false);
            return ViewUtil.render(request, model, Path.Template.POLL_INTELLIGENCE_TYPE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitPollIntelligenceType = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            // todo: obrada pitanja, spremanje reszultata za user-a (intelligence type), oznaciti useru da je ispunio anketu
            HashMap<String, Object> model = new HashMap<>();
            model.put("validation", "Anketa je uspješno ispunjena, zahvaljujemo!");
            return ViewUtil.render(request, model, Path.Template.POLL_INTELLIGENCE_TYPE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

}
