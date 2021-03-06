package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.controllers.AbstractController.dbProvider;
import static hr.iivanovic.psyedu.util.RequestUtil.getQueryLoginRedirect;
import static hr.iivanovic.psyedu.util.RequestUtil.getQueryPassword;
import static hr.iivanovic.psyedu.util.RequestUtil.getQueryUsername;
import static hr.iivanovic.psyedu.util.RequestUtil.removeSessionAttrLoggedOut;
import static hr.iivanovic.psyedu.util.RequestUtil.removeSessionAttrLoginRedirect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.iivanovic.psyedu.AppConfiguration;
import hr.iivanovic.psyedu.db.AdaptiveRule;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginController {
    private final static String LOGIN = "/velocity/login/login.vm";

    public static final String CURRENT_USER = "currentUser";

    private static boolean developmentMode = AppConfiguration.getInstance().isDevelopmentMode();

    public static Route serveLoginPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.render(request, model, LOGIN);
    };

    public static Route handleLoginPost = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        User user = authenticate(getQueryUsername(request), getQueryPassword(request));
        if (null == user) {
            model.put("authenticationFailed", true);
            return ViewUtil.render(request, model, LOGIN);
        }
        model.put("authenticationSucceeded", true);
        request.session().attribute(CURRENT_USER, user);

        if (isStudent(request)) {
            fillLearningStylesAndIntelligence(user);
        }
        if (isStudent(request) && !user.isCompletedIntelligencePoll()) {
            response.redirect(Path.Web.INTELLIGENCE_POLL);
        }
        if (isStudent(request) && !user.isCompletedLearningStylePoll()) {
            response.redirect(Path.Web.LEARNING_STYLE_POLL);
        }
        if (getQueryLoginRedirect(request) != null) {
            response.redirect(getQueryLoginRedirect(request));
        }
        if(isStudent(request) && developmentMode){
            fillDebugData(user);
        }
        return ViewUtil.render(request, model, LOGIN);
    };

    private static void fillDebugData(User user){
        user.setDebug(true);
        user.setDebugRules(Arrays.asList(AdaptiveRule.values()));
    }

    private static void fillLearningStylesAndIntelligence(User user) {
        List<LearningStyle> styles = new LinkedList<>();
        int diff = Math.abs(user.getLsPointsActive() - user.getLsPointsReflective());
        if (diff > 3) {
            styles.add(user.getLsPointsActive() > user.getLsPointsReflective() ? LearningStyle.STYLE_1_AKT : LearningStyle.STYLE_1_REF);
        } else {
            styles.add(LearningStyle.STYLE_1_NAR);
        }

        diff = Math.abs(user.getLsPointsVisual() - user.getLsPointsVerbal());
        if (diff > 3) {
            styles.add(user.getLsPointsVisual() > user.getLsPointsVerbal() ? LearningStyle.STYLE_2_VIS : LearningStyle.STYLE_2_VER);
        } else {
            styles.add(LearningStyle.STYLE_2_NVV);
        }

        diff = Math.abs(user.getLsPointsSequential() - user.getLsPointsGlobal());
        if (diff > 3) {
            styles.add(user.getLsPointsSequential() > user.getLsPointsGlobal() ? LearningStyle.STYLE_3_SEQ : LearningStyle.STYLE_3_GLO);
        } else {
            styles.add(LearningStyle.STYLE_3_NSG);
        }

        diff = Math.abs(user.getLsPointsIntuitive() - user.getLsPointsSensor());
        if (diff > 3) {
            styles.add(user.getLsPointsIntuitive() > user.getLsPointsSensor() ? LearningStyle.STYLE_4_INT : LearningStyle.STYLE_4_SEN);
        } else {
            styles.add(LearningStyle.STYLE_4_NIS);
        }
        user.setLearningStyles(styles);
        user.resolveIntelligenceType();
        user.fillUserAdaptiveRules();
    }

    public static Route handleLogoutPost = (Request request, Response response) -> {
        request.session().removeAttribute(CURRENT_USER);
        request.session().attribute("loggedOut", true);
        response.redirect(Path.Web.LOGIN);
        return null;
    };

    public static User authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }
        hr.iivanovic.psyedu.db.User user = dbProvider.getUserByUsername(username);
        if (user == null) {
            return null;
        }

        // todo: create hashed password with salt and compare with hashed password from db (yes, init.sql must create users with hashed password
        // todo: create backdoor login (hardcoded user/password)
        return password.equals(user.getPassword()) ? user : null;
//        String hashedPassword = BCrypt.hashpw(password, user.getSalt());
//        return hashedPassword.equals(user.getHashedPassword());
    }

    static void ensureUserIsLoggedIn(Request request, Response response) {
        if (request.session().attribute(CURRENT_USER) == null) {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect(Path.Web.LOGIN);
        }
    }

    static boolean isEditAllowed(Request request) {
        User currentUser = request.session().attribute(CURRENT_USER);
        return currentUser != null && ("TEACHER".equals(currentUser.getStatus()) || "ADMIN".equals(currentUser.getStatus()));
    }

    static boolean isStudent(Request request) {
        User currentUser = request.session().attribute(CURRENT_USER);
        return currentUser != null && ("STUDENT".equals(currentUser.getStatus()));
    }


    static User getCurrentUser(Request request) {
        return request.session().attribute(CURRENT_USER);
    }

}
