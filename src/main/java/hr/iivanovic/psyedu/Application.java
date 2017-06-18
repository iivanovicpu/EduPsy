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

import hr.iivanovic.psyedu.controllers.AdaptiveRulesController;
import hr.iivanovic.psyedu.controllers.AdminSubjectsController;
import hr.iivanovic.psyedu.controllers.ExamController;
import hr.iivanovic.psyedu.controllers.IndexController;
import hr.iivanovic.psyedu.controllers.LoginController;
import hr.iivanovic.psyedu.controllers.PollController;
import hr.iivanovic.psyedu.controllers.ProfileController;
import hr.iivanovic.psyedu.controllers.StudentsController;
import hr.iivanovic.psyedu.controllers.SubjectQuestionsController;
import hr.iivanovic.psyedu.controllers.SubjectsController;
import hr.iivanovic.psyedu.controllers.UploadController;
import hr.iivanovic.psyedu.db.AdaptiveRule;
import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Filters;
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
            if (isChecked) {
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
        get("/index/", IndexController.serveIndexPage);

        // upload todo: napravit odvojeni controller
        post("/upload/", "multipart/form-data", UploadController.uploadFile);
        get("/upload/:subjectId/", UploadController.uploadForm);
        post("/deletelink/", UploadController.deleteExternalLink);

        get("/onetitlequestions/:subjectid/", SubjectQuestionsController.fetchOneTitleForAddQuestions);
        post("/onetitlequestions/:subjectid/", SubjectQuestionsController.submitQuestion);
        post("/deletequestion/", SubjectQuestionsController.deleteQuestion);

        get("/addsubject/", AdminSubjectsController.addNewSubject);
        post("/addsubject/", AdminSubjectsController.submitAddedSubject);

        get("/onetitle/:id/", SubjectsController.fetchOneTitle);
        get("/onetitlechild/:id/", SubjectsController.fetchOneChildTitle);
        post("/onetitlechild/:id/", SubjectsController.submitOneTitleStatus);

        get("/subjects/", AdminSubjectsController.fetchAllParentSubjects);
        get("/edit_subject/:id/", AdminSubjectsController.fetchSubjectForEdit);
        get("/edit_subject_item/:id/:parentId/:action/", AdminSubjectsController.editSubjectItem);
        post("/edit_subject_item/:id/:parentId/:action/", AdminSubjectsController.submitEditedSubject);

        get("/exam/:subjectid/", ExamController.fetchQuestionsForTitle);
        post("/exam/:subjectid/", ExamController.submitExamQuestions);

        get("/students/", StudentsController.fetchAllStudents);
        get( "/addstudent/", StudentsController.addStudent);
        post("/addstudent/", StudentsController.submitStudent);
        get("/studentdtl/:id/", StudentsController.fetchStudentDetails);

        get("/profile/", ProfileController.fetchPersonalProfile);
        post("/profile/", ProfileController.submitPersonalProfile);

        get("/adminrules/", AdaptiveRulesController.fetchAllRulesForAdmin);

        get("/intelligencepoll/", PollController.showPollIntelligenceType);
        post("/intelligencepoll/", PollController.submitPollIntelligenceType);

        get("/learningstylepoll/", PollController.showPollLearningStyle);
        post("/learningstylepoll/", PollController.submitPollLearningStyle);

//        post(Path.Web.DEBUG, DebugController.submitDebugRules);

        get("/login/", LoginController.serveLoginPage);
        post("/login/", LoginController.handleLoginPost);

        post("/logout/", LoginController.handleLogoutPost);

        get("*", ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*", Filters.addGzipHeader);

    }

}
