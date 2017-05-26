package hr.iivanovic.psyedu;

import static hr.iivanovic.psyedu.controllers.LoginController.CURRENT_USER;
import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.debug.DebugScreen.enableDebugScreen;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import hr.iivanovic.psyedu.controllers.AdaptiveRulesController;
import hr.iivanovic.psyedu.controllers.AdminSubjectsController;
import hr.iivanovic.psyedu.controllers.DebugController;
import hr.iivanovic.psyedu.controllers.ExamController;
import hr.iivanovic.psyedu.controllers.PollController;
import hr.iivanovic.psyedu.controllers.ProfileController;
import hr.iivanovic.psyedu.controllers.StudentsController;
import hr.iivanovic.psyedu.controllers.SubjectQuestionsController;
import hr.iivanovic.psyedu.controllers.UploadController;
import hr.iivanovic.psyedu.db.AdaptiveRule;
import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.controllers.IndexController;
import hr.iivanovic.psyedu.controllers.SubjectsController;
import hr.iivanovic.psyedu.controllers.LoginController;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Filters;
import hr.iivanovic.psyedu.util.InitDb;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;

public class Application {

    public static void main(String[] args) {

        // Configure Spark
        port(4567);
        // static files in classpath
        staticFiles.location("/public");

        staticFiles.externalLocation(AppConfiguration.getInstance().getExternalLocation());
        staticFiles.expireTime(600L);
        enableDebugScreen();

        Model model = Sql2oModel.getInstance();

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

        post("/testrules/", (request, response) -> {
            int adaptiveRuleId = Integer.parseInt(request.queryParams("ruleId"));
            boolean isChecked = Boolean.parseBoolean(request.queryParams("value"));
            response.status(200);
            response.type("application/json");
            AdaptiveRule adaptiveRule = AdaptiveRule.getById(adaptiveRuleId);
            User user = request.session().attribute(CURRENT_USER);
            if(isChecked) {
                user.addAdaptiveRule(adaptiveRule);
            } else {
                user.removeAdaptiveRule(adaptiveRule);
            }
            return dataToJson(adaptiveRule);
        });

        // Set up before-filters (called before each get/post)
        before("*", Filters.addTrailingSlashes);
        before("*", Filters.handleLocaleChange);

        // Set up routes
        get(Path.Web.INDEX, IndexController.serveIndexPage);

        // upload todo: napravit odvnojeni controller
        post("/upload/","multipart/form-data", UploadController.uploadFile);
        post("/deletelink/", AdminSubjectsController.deleteExternalLink);
        get(Path.Web.VIEW_SUBJECT, SubjectsController.fetchOneSubject);
        get(Path.Web.ONE_SUBJECT_QUESTIONS, SubjectQuestionsController.fetchtitlesForAddQuestions);
        get("/onetitlequestions/:subjectid/", SubjectQuestionsController.fetchOneTitleForAddQuestions);
        post(Path.Web.ONE_TITLE_QUESTIONS, SubjectQuestionsController.submitQuestion);
        post("/deletequestion/",SubjectQuestionsController.deleteQuestion);
        get(Path.Web.ADD_SUBJECT, SubjectsController.addNewSubject);
        post(Path.Web.ADD_SUBJECT, AdminSubjectsController.submitAddedSubject);
        get(Path.Web.ONE_PARENT_TITLE, SubjectsController.fetchOneTitle);
        get(Path.Web.ONE_CHILD_TITLE, SubjectsController.fetchOneChildTitle);
        post(Path.Web.ONE_CHILD_TITLE, SubjectsController.submitOneTitleStatus);

        get(Path.Web.SUBJECTS, AdminSubjectsController.fetchAllParentSubjects);
        get(Path.Web.EDIT_SUBJECT, AdminSubjectsController.fetchSubjectForEdit);
        get(Path.Web.EDIT_SUBJECT_ITEM, AdminSubjectsController.editSubjectItem);
        post(Path.Web.EDIT_SUBJECT_ITEM, AdminSubjectsController.submitEditedSubject);

        get(Path.Web.EXAM, ExamController.fetchQuestionsForTitle);
        post(Path.Web.EXAM, ExamController.submitExamQuestions);

        get(Path.Web.STUDENTS, StudentsController.fetchAllStudents);
        get(Path.Web.ADDSTUDENT, StudentsController.addStudent);
        post(Path.Web.ADDSTUDENT, StudentsController.submitStudent);
        get(Path.Web.STUDENT_DETAILS, StudentsController.fetchStudentDetails);

        get(Path.Web.PERSONAL_PROFILE, ProfileController.fetchPersonalProfile);
        post(Path.Web.PERSONAL_PROFILE, ProfileController.submitPersonalProfile);

        get(Path.Web.ADMIN_RULES, AdaptiveRulesController.fetchAllRulesForAdmin);

        get(Path.Web.INTELLIGENCE_POLL, PollController.showPollIntelligenceType);
        post(Path.Web.INTELLIGENCE_POLL, PollController.submitPollIntelligenceType);

        get(Path.Web.LEARNING_STYLE_POLL, PollController.showPollLearningStyle);
        post(Path.Web.LEARNING_STYLE_POLL, PollController.submitPollLearningStyle);

//        post(Path.Web.DEBUG, DebugController.submitDebugRules);

        get(Path.Web.LOGIN, LoginController.serveLoginPage);
        post(Path.Web.LOGIN, LoginController.handleLoginPost);
        post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        get("*", ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*", Filters.addGzipHeader);

    }

}
