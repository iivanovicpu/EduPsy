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
        int verbal = 0;
        int nonVerbal = 0;
        int mathLogic = 0;
        /*
        *   V=odgovovor2+3+4+7+11+15+17+23+24+27
            ML=odgovor1+5+6+8+9+14+16+19+22+25
            NV=odgovor10+12+13+18+20+21+26+28+29+30
        */
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            for (int i = 1; i < 31; i++) {
                String odgovor = request.queryParams("odgovor" + i);
                if (null != odgovor) {
                    if (i == 2 || i == 3 || i == 4 || i == 7 || i == 11 || i == 15 || i == 17 | i == 23 || i == 24 || i == 27) {
                        verbal += Integer.valueOf(odgovor);
                    }
                    if (i == 1 || i == 5 || i == 6 || i == 8 || i == 9 || i == 14 || i == 16 | i == 19 || i == 22 || i == 25) {
                        mathLogic += Integer.valueOf(odgovor);
                    }
                    if (i == 10 || i == 12 || i == 13 || i == 18 || i == 20 || i == 21 || i == 26 | i == 28 || i == 29 || i == 30) {
                        nonVerbal += Integer.valueOf(odgovor);
                    }
                }
            }
            int intelligenceTypeId = 0;
            if(verbal > nonVerbal && verbal > mathLogic){
                intelligenceTypeId = IntelligenceTypes.V.getId();
            }
            if(nonVerbal > verbal && nonVerbal > mathLogic){
                intelligenceTypeId = IntelligenceTypes.NV.getId();
            }
            if(mathLogic > verbal && mathLogic > nonVerbal){
                intelligenceTypeId = IntelligenceTypes.ML.getId();
            }
            dbProvider.updateStudentIntelligenceType(user.getId(), intelligenceTypeId);
            model.put("validation", "Anketa je uspješno ispunjena, zahvaljujemo!");
            return ViewUtil.render(request, model, Path.Template.POLL_INTELLIGENCE_TYPE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route showPollLearningStyle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("validation", false);
            return ViewUtil.render(request, model, Path.Template.POLL_LEARNING_STYLE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitPollLearningStyle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            for (int i = 1; i < 45; i++) {
                String odgovor = request.queryParams("odgovor" + i);
                System.out.println("odgovor" + i + " = " + odgovor);
                // todo: algoritam za zbranje
            }
            model.put("validation", false);
            return ViewUtil.render(request, model, Path.Template.POLL_LEARNING_STYLE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

}
