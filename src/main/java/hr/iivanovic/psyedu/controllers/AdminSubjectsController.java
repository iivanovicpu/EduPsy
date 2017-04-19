package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsJson;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.db.SubjectLevel;
import hr.iivanovic.psyedu.db.SubjectPosition;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.utils.StringUtils;

/**
 * @author iivanovic
 * @date 29.08.16.
 */
public class AdminSubjectsController extends AbstractController {

    public static Route fetchAllParentSubjects = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("subjects", dbProvider.getAllParentSubjects());
            model.put("editAllowed", LoginController.isEditAllowed(request));
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ALL);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getAllSubjects());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route fetchSubjectForEdit = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (!LoginController.isEditAllowed(request)) {
            return ViewUtil.notAcceptable.handle(request, response);
        }
        int subjectId = Integer.parseInt(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            List<Subject> subjects = dbProvider.getSubjectsForEdit(subjectId);
            model.put("subjects", subjects);
            model.put("successmsg", "");
            return ViewUtil.render(request, model, Path.Template.EDIT_SUBJECT);

        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route editSubjectItem = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (LoginController.isStudent(request)) {
            return ViewUtil.notAcceptable.handle(request, response);
        }
        int subjectId = Integer.parseInt(request.params("id"));
        int parentSubjectId = Integer.parseInt(request.params("parentId"));
        String action = request.params("action");
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("editAllowed", true);
            model.put("action", action);
            model.put("subjectId", subjectId);
            model.put("parentSubjectId", parentSubjectId);
            Subject subject = new Subject();
            if ("edit".equals(action)) {
                subject = dbProvider.getSubject(subjectId);
            }
            model.put("subject", subject);
            return ViewUtil.render(request, model, Path.Template.EDIT_SUBJECT_ITEM);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitEditedSubject = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        HashMap<String, Object> model = new HashMap<>();
        if (clientAcceptsHtml(request) && LoginController.isEditAllowed(request)) {
            String action = request.queryParams("action");
            int subjectId = Integer.parseInt(request.queryParams("subjectId"));
            int parentSubjectId = Integer.parseInt(request.queryParams("parentSubjectId"));

            String title = request.queryParams("title");
            String keywords = request.queryParams("keywords");
            String content = request.queryParams("content");
            String additionalContent = request.queryParams("additionalContent");
            Subject subject;
            if ("edit".equals(action)) {
                subject = dbProvider.getSubject(subjectId);
                subject.setAdditionalContent(additionalContent);
                subject.setContent(content);
                subject.setKeywords(keywords);
                subject.setTitle(title);
                dbProvider.updateSubject(subject);
            }
            if ("add".equals(action)) {
                Subject parentSubject = dbProvider.getSubject(subjectId);
                SubjectPosition subPosition = SubjectPosition.getById(parentSubject.getSubjectPositionId()).getSubPosition();
                subject = new Subject(0, title, keywords, null, subjectId, parentSubjectId, SubjectLevel.OSNOVNO.getId(), 0, content, additionalContent, subPosition.getId(), subPosition);
                dbProvider.createSubSubject(subject);
            }

            model.put("successmsg", "update success");

            response.redirect(Path.Web.EDIT_SUBJECT.replaceFirst(":id", String.valueOf(parentSubjectId)));
            return null;
        } else {
            model.put("successmsg", "update fail");
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static final Logger LOGGER = LoggerFactory.getLogger(AdminSubjectsController.class);


    private static String validateSubject(String title) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(title)) {
            sb.append("naslov je obavezan podatak\n");
        } else {
            dbProvider.getAllSubjects().stream().filter(subject -> subject.getTitle().equals(title))
                    .forEach(subject -> sb.append("naslov:\" ").append(title).append("\" već postoji u bazi!"));
        }
        return sb.toString();
    }
}
