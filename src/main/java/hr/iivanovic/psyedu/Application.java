package hr.iivanovic.psyedu;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.debug.DebugScreen.enableDebugScreen;

import org.sql2o.Sql2o;

import hr.iivanovic.psyedu.book.BookController;
import hr.iivanovic.psyedu.book.BookDao;
import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.index.IndexController;
import hr.iivanovic.psyedu.learning.LearningController;
import hr.iivanovic.psyedu.login.LoginController;
import hr.iivanovic.psyedu.user.UserDao;
import hr.iivanovic.psyedu.util.DbUtil;
import hr.iivanovic.psyedu.util.Filters;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;

public class Application {

    // Declare dependencies
    public static BookDao bookDao;
    public static UserDao userDao;

    public static void main(String[] args) {

        // Instantiate your dependencies
        bookDao = new BookDao();
        userDao = new UserDao();

        // Configure Spark
        port(4567);
        // static files in classpath
        staticFiles.location("/public");

        // static files outside classpath (todo: load from settings)
        staticFiles.externalLocation("/home/iivanovic/edupsy/");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        Sql2o sql2o = DbUtil.getH2DataSource();
        Model model = new Sql2oModel(sql2o);

        /* rest api */
        get("/api/subjects/", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return dataToJson(model.getAllSubjects());
        });

        get("/api/subject/:id/", (request, response) -> {
            long id = Long.parseLong(request.params(":id"));
            response.status(200);
            response.type("application/json");
            return dataToJson(model.getSubject(id));
        });

        // Set up before-filters (called before each get/post)
        before("*",                  Filters.addTrailingSlashes);
        before("*",                  Filters.handleLocaleChange);

        // Set up routes
        get(Path.Web.INDEX,          IndexController.serveIndexPage);
        get(Path.Web.BOOKS,          BookController.fetchAllBooks);
        get(Path.Web.SUBJECTS,       LearningController.fetchAllSubjects);
        get(Path.Web.ONE_SUBJECTS,   LearningController.fetchOneSubject);
        post(Path.Web.ONE_SUBJECTS,  LearningController.fetchOneSubjectEdit);
        post(Path.Web.EDIT_SUBJECT,  LearningController.submitEditedSubject);
        get(Path.Web.ONE_BOOK,       BookController.fetchOneBook);
        get(Path.Web.LOGIN,          LoginController.serveLoginPage);
        post(Path.Web.LOGIN,         LoginController.handleLoginPost);
        post(Path.Web.LOGOUT,        LoginController.handleLogoutPost);
        get("*",                     ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*",                   Filters.addGzipHeader);

    }

}
