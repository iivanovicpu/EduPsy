package hr.iivanovic.psyedu.learning;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsJson;

import java.util.HashMap;

import org.sql2o.Sql2o;

import hr.iivanovic.psyedu.controllers.AbstractController;
import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.login.LoginController;
import hr.iivanovic.psyedu.util.DbUtil;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 29.08.16.
 */
public class LearningController extends AbstractController {

    public static Route fetchAllSubjects = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("subjects", dbProvider.getAllSubjects());
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ALL);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getAllSubjects());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneSubject = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        long id = Long.parseLong(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = dbProvider.getSubject(id);
            model.put("subject", subject);
            model.put("editAllowed",LoginController.isEditAllowed(request));
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ONE);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getSubject(id));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneSubjectEdit = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        long id = Long.parseLong(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = dbProvider.getSubject(id);
            model.put("subject", subject);
            model.put("editAllowed",LoginController.isEditAllowed(request));
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ONE_EDIT);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getSubject(id));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

}
