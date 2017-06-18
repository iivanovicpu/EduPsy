package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;

import java.util.HashMap;
import java.util.Objects;

import hr.iivanovic.psyedu.db.IntelligenceType;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 26.10.16.
 */
public class PollController extends AbstractController {
    private final static String POLL_INTELLIGENCE_TYPE = "/velocity/pollIntelligenceType.vm";
    private final static String POLL_LEARNING_STYLE = "/velocity/pollLearningType.vm";


    public static Route showPollIntelligenceType = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("validation", false);
            return ViewUtil.render(request, model, POLL_INTELLIGENCE_TYPE);
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
            dbProvider.updateStudentIntelligenceTypePoints(user.getId(), verbal, nonVerbal, mathLogic);
            user.resolveIntelligenceType();
            model.put("validation", "Anketa je uspješno ispunjena, zahvaljujemo!");
            return ViewUtil.render(request, model, POLL_INTELLIGENCE_TYPE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route showPollLearningStyle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("validation", false);
            return ViewUtil.render(request, model, POLL_LEARNING_STYLE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitPollLearningStyle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User user = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            int aktivni = 0;
            int reflektivni = 0;
            int opazajni = 0;
            int intuitivni = 0;
            int vizualni = 0;
            int verbalni = 0;
            int sekvencijalni = 0;
            int globalni = 0;
            HashMap<String, Object> model = new HashMap<>();
            for (int i = 1; i < 45; i++) {
                String odgovor = request.queryParams("odgovor" + i);
                if (null != odgovor) {
                    if (i == 1 || i == 5 || i == 9 || i == 13 || i == 17 || i == 21 || i == 25 || i == 29 || i == 33 || i == 37 || i == 41) {
                        if (Objects.equals(odgovor, "a"))
                            aktivni++;
                        else
                            reflektivni++;
                    }
                    if (i == 2 || i == 6 || i == 10 || i == 14 || i == 18 || i == 22 || i == 26 || i == 30 || i == 34 || i == 38 || i == 42) {
                        if (Objects.equals(odgovor, "a"))
                            opazajni++;
                        else
                            intuitivni++;
                    }
                    if (i == 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23 || i == 27 || i == 31 || i == 35 || i == 39 || i == 43) {
                        if (Objects.equals(odgovor, "a"))
                            vizualni++;
                        else
                            verbalni++;
                    }
                    if (i == 4 || i == 8 || i == 12 | i == 16 || i == 20 || i == 24 || i == 28 || i == 32 || i == 36 || i == 40 || i == 44) {
                        if (Objects.equals(odgovor, "a"))
                            sekvencijalni++;
                        else
                            globalni++;
                    }
                }
            }
            dbProvider.updateStudentLearningStylePollResult(user.getId(), aktivni, reflektivni, opazajni, intuitivni, vizualni, verbalni, sekvencijalni, globalni);
            model.put("validation", "Anketa je uspješno ispunjena, zahvaljujemo!");
            return ViewUtil.render(request, model, POLL_LEARNING_STYLE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

}
