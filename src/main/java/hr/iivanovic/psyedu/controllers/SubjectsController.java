package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsJson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import hr.iivanovic.psyedu.AppConfiguration;
import hr.iivanovic.psyedu.controllers.view.SubjectView;
import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.db.TitleLearningStatus;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.html.HtmlParser;
import hr.iivanovic.psyedu.html.TitleLink;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 29.08.16.
 */
public class SubjectsController extends AbstractController {

    private static HtmlParser htmlParser = HtmlParser.getInstance();

    public static Route fetchOneSubject = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        long subjectId = Integer.parseInt(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = dbProvider.getSubject(subjectId);
            model.put("subject", subject);
            model.put("successmsg", "");
            boolean isStudent = !LoginController.isEditAllowed(request);
            model.put("isStudent", isStudent);
            if (isStudent) {
                User student = LoginController.getCurrentUser(request);
                File file = new File(AppConfiguration.getInstance().getExternalLocation() + subject.getUrl());
                List<TitleLink> titleLinks = htmlParser.getAllSubjectsLinks(file, subject.getUrl(), subject.getId(), student);
                model.put("titles", titleLinks);
            } else {
                model.put("titles", new LinkedList<String>());
            }
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ONE);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getSubject(subjectId));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneTitle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User currentUser = LoginController.getCurrentUser(request);
        int parentSubjectId = Integer.parseInt(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            SubjectView subjectView = new SubjectView(dbProvider.getSubject(parentSubjectId),currentUser);
            List<SubjectView> subjects = subjectView.getSubjectViewsForSameParent();
            model.put("subjects", subjects);
            model.put("nextParent", subjectView.getNextParentId());
            model.put("nextChild", subjectView.getNextChildId());
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ONE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneChildTitle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User currentUser = LoginController.getCurrentUser(request);
        int id = Integer.parseInt(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = dbProvider.getSubject(id);
            SubjectView subjectView = new SubjectView(subject, currentUser);
            model.put("subject", subjectView);

            model.put("sidebarContent", subjectView.createSidebarNavigation());

            dbProvider.logLearningStatus(currentUser.getId(), subject.getId(), TitleLearningStatus.OPENED.getId());

            return ViewUtil.render(request, model, Path.Template.SUBJECT_ONE_TITLE);

        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitOneTitleStatus = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        String titleId = request.params("id");
        int subjectId = Integer.parseInt(request.params("subjectid"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            if (LoginController.isStudent(request)) {
                User student = LoginController.getCurrentUser(request);
                dbProvider.logLearningStatus(student.getId(), subjectId, TitleLearningStatus.LEARNED.getId());
                model.put("status", TitleLearningStatus.LEARNED.getId());
                model.put("subjectId", subjectId);
                model.put("titleId", titleId);
                Subject subject = dbProvider.getSubject(subjectId);
                String content = htmlParser.getOneTitleContent(AppConfiguration.getInstance().getExternalLocation() + subject.getUrl().substring(1, subject.getUrl().length()), titleId);
                model.put("content", content);
                return ViewUtil.render(request, model, Path.Template.SUBJECT_ONE_TITLE);
            }
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    @Deprecated
    public static Route submitEditedSubject = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        long id = Long.parseLong(request.queryParams("id"));
        String document = request.queryParams("doc");
        String finalizedDoc = htmlParser.improveDocument(document);

        HashMap<String, Object> model = new HashMap<>();
        if (clientAcceptsHtml(request) && LoginController.isEditAllowed(request)) {
            Subject subject = dbProvider.getSubject(id);

            File sourceFile = new File(AppConfiguration.getInstance().getExternalLocation() + subject.getUrl());
            File backupFile = new File(AppConfiguration.getInstance().getExternalLocation() + subject.getUrl() + "-" + now() + ".backup");

            copyFileUsingStream(sourceFile, backupFile);

            try {
                FileWriter fileWriter = new FileWriter(sourceFile);
                fileWriter.write(finalizedDoc);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            model.put("successmsg", "update success");

            response.redirect(Path.Web.getVIEW_SUBJECT().replaceFirst(":id", String.valueOf(subject.getId())));
            return null;
        } else {
            model.put("successmsg", "update fail");
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getSubject(id));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route addNewSubject = (Request request, Response response) -> {
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = new Subject();
            model.put("subject", subject);
            model.put("validation", false);
            model.put("editAllowed", LoginController.isEditAllowed(request));
            return ViewUtil.render(request, model, Path.Template.SUBJECT_ADD);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            assert is != null;
            is.close();
            assert os != null;
            os.close();
        }
    }

    public static String now() {
        String validPatterns5 = "dd.MM.yyyy-HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(validPatterns5);
        return LocalDateTime.now().format(formatter);
    }
}
