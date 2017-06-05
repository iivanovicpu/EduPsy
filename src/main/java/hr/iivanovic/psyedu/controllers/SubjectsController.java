package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import hr.iivanovic.psyedu.AppConfiguration;
import hr.iivanovic.psyedu.controllers.view.ExternalLinkView;
import hr.iivanovic.psyedu.controllers.view.SubjectView;
import hr.iivanovic.psyedu.db.ExternalLink;
import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.db.TitleLearningStatus;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.html.HtmlParser;
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

    public static Route fetchOneTitle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        User currentUser = LoginController.getCurrentUser(request);
        int parentSubjectId = Integer.parseInt(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            SubjectView subjectView = new SubjectView(dbProvider.getSubject(parentSubjectId), currentUser);
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
            model.put("links", createExternalLinkView(dbProvider.getAllExternalLinksBySubjectId(id)));

            dbProvider.logLearningStatus(currentUser.getId(), subject.getId(), TitleLearningStatus.OPENED.getId());

            return ViewUtil.render(request, model, Path.Template.SUBJECT_ONE_TITLE);

        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static List<ExternalLinkView> createExternalLinkView(List<ExternalLink> allExternalLinksBySubjectId) {
        return allExternalLinksBySubjectId.stream().map(ExternalLinkView::new).collect(Collectors.toCollection(LinkedList::new));
    }

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

}
