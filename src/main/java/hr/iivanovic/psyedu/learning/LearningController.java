package hr.iivanovic.psyedu.learning;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsJson;

import java.util.HashMap;

import org.sql2o.Sql2o;

import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
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
public class LearningController {

    public static Route fetchAllBooks = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Sql2o sql2o = DbUtil.getH2DataSource();
        Model dbProvider = new Sql2oModel(sql2o);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("books", dbProvider.getAllSubjects());
            return ViewUtil.render(request, model, Path.Template.LEARNING_ALL);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getAllSubjects());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
