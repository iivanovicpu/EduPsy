package hr.iivanovic.psyedu;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.debug.DebugScreen.enableDebugScreen;

import hr.iivanovic.psyedu.controllers.AdaptiveRulesController;
import hr.iivanovic.psyedu.controllers.ExamController;
import hr.iivanovic.psyedu.controllers.PollController;
import hr.iivanovic.psyedu.controllers.ProfileController;
import hr.iivanovic.psyedu.controllers.StudentsController;
import hr.iivanovic.psyedu.controllers.SubjectQuestionsController;
import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.controllers.IndexController;
import hr.iivanovic.psyedu.controllers.SubjectsController;
import hr.iivanovic.psyedu.controllers.LoginController;
import hr.iivanovic.psyedu.util.Filters;
import hr.iivanovic.psyedu.util.InitDb;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;

public class Application {
    public static boolean developmentMode = AppConfiguration.getInstance().isDevelopmentMode();

    public static void main(String[] args) {

        // Configure Spark
        port(4567);
        // static files in classpath
        staticFiles.location("/public");

        staticFiles.externalLocation(AppConfiguration.getInstance().getExternalLocation());
        staticFiles.expireTime(600L);
        enableDebugScreen();

//        Sql2o sql2o = DbUtil.getH2DataSource();
        Model model = Sql2oModel.getInstance();
        if (developmentMode) {
            InitDb initDb = new InitDb();
            initDb.init();
            developmentMode = false;
        }

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
        before("*", Filters.addTrailingSlashes);
        before("*", Filters.handleLocaleChange);

        // Set up routes
        get(Path.Web.INDEX, IndexController.serveIndexPage);

        get(Path.Web.SUBJECTS, SubjectsController.fetchAllSubjects);
        get(Path.Web.VIEW_SUBJECT, SubjectsController.fetchOneSubject);
        get(Path.Web.EDIT_SUBJECT, SubjectsController.fetchOneSubjectEdit);
        get(Path.Web.ONE_SUBJECT_QUESTIONS, SubjectQuestionsController.fetchtitlesForAddQuestions);
        get(Path.Web.ONE_TITLE_QUESTIONS, SubjectQuestionsController.fetchOneTitleForAddQuestions);
        post(Path.Web.ONE_TITLE_QUESTIONS, SubjectQuestionsController.submitQuestion);
        post(Path.Web.SUBMIT_EDITED_SUBJECT, SubjectsController.submitEditedSubject);
        get(Path.Web.ADD_SUBJECT, SubjectsController.addNewSubject);
        post(Path.Web.ADD_SUBJECT, SubjectsController.submitAddedSubject);
        get(Path.Web.ONE_TITLE, SubjectsController.fetchOneTitle);
        post(Path.Web.ONE_TITLE, SubjectsController.submitOneTitleStatus);

        get(Path.Web.EXAM, ExamController.fetchQuestionsForTitle);
        post(Path.Web.EXAM, ExamController.submitExamQuestions);

        get(Path.Web.STUDENTS, StudentsController.fetchAllStudents);
        get(Path.Web.ADDSTUDENT, StudentsController.addStudent);
        post(Path.Web.ADDSTUDENT, StudentsController.submitStudent);

        get(Path.Web.PERSONAL_PROFILE, ProfileController.fetchPersonalProfile);
        post(Path.Web.PERSONAL_PROFILE, ProfileController.submitPersonalProfile);

        get(Path.Web.ADMIN_RULES, AdaptiveRulesController.fetchAllRulesForAdmin);
        post(Path.Web.ADMIN_RULES, AdaptiveRulesController.submitRule);

        get(Path.Web.INTELLIGENCE_POLL, PollController.showPollIntelligenceType);
        post(Path.Web.INTELLIGENCE_POLL, PollController.submitPollIntelligenceType);

        get(Path.Web.LEARNING_STYLE_POLL, PollController.showPollLearningStyle);
        post(Path.Web.LEARNING_STYLE_POLL, PollController.submitPollLearningStyle);

        get(Path.Web.LOGIN, LoginController.serveLoginPage);
        post(Path.Web.LOGIN, LoginController.handleLoginPost);
        post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        get("*", ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*", Filters.addGzipHeader);

    }

}
