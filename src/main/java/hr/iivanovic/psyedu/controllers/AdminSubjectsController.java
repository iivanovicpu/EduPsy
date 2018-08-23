package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsJson;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.iivanovic.psyedu.AppConfiguration;
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
    private final static String SUBJECT_ADD = "/velocity/subjects/add.vm";
    private final static String SUBJECTS_ALL = "/velocity/subjects/all.vm";
    private final static String EDIT_SUBJECT = "/velocity/subjects/edit_subject.vm";
    private final static String EDIT_SUBJECT_ITEM = "/velocity/subjects/edit_subject_item.vm";

    public static Route addNewSubject = (Request request, Response response) -> {
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = new Subject();
            model.put("subject", subject);
            model.put("validation", false);
            model.put("editAllowed", LoginController.isEditAllowed(request));
            return ViewUtil.render(request, model, SUBJECT_ADD);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitAddedSubject = (request, response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);
        String title = request.queryParams("title");
        String keywords = request.queryParams("keywords");
        HashMap<String, Object> model = new HashMap<>();
        String validationMsg = validateSubject(title);
        if (!StringUtils.isEmpty(validationMsg)) {
            model.put("validation", validationMsg);
            model.put("editAllowed", LoginController.isEditAllowed(request));
            return ViewUtil.render(request, model, SUBJECT_ADD);
        }
        String titleReplaced = title.replaceAll(" ", "").replaceAll("[^\\x00-\\x7F]", "");
        String filename = titleReplaced.substring(0, titleReplaced.length() > 10 ? 10 : titleReplaced.length()).toLowerCase();
        String filenamePath = "/materijali/".concat(filename);
        createDirIfNotExists(filenamePath, title);

        dbProvider.createSubject(title, keywords, filenamePath, SubjectLevel.OSNOVNO, SubjectPosition.PREDMET.getId());

        if (clientAcceptsHtml(request)) {
            response.redirect(Path.Web.getSUBJECTS());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route fetchAllParentSubjects = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("subjects", dbProvider.getAllParentSubjects());
            model.put("editAllowed", LoginController.isEditAllowed(request));
            return ViewUtil.render(request, model, SUBJECTS_ALL);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getAllSubjects());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route fetchSubjectForEdit = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);
        int subjectId = Integer.parseInt(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            List<Subject> subjects = dbProvider.getSubjectsByParentSubjectId(subjectId);
            model.put("subjects", subjects);
            model.put("successmsg", "");
            return ViewUtil.render(request, model, EDIT_SUBJECT);

        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route editSubjectItem = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);

        int subjectId = Integer.parseInt(request.params("id"));
        int parentSubjectId = Integer.parseInt(request.params("parentId"));
        String action = request.params("action");
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
        return ViewUtil.render(request, model, EDIT_SUBJECT_ITEM);
    };

    public static Route submitEditedSubject = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);

        HashMap<String, Object> model = new HashMap<>();
        if (clientAcceptsHtml(request) && LoginController.isEditAllowed(request)) {
            String action = request.queryParams("action");
            int subjectId = Integer.parseInt(request.queryParams("subjectId"));
            int parentSubjectId = Integer.parseInt(request.queryParams("parentSubjectId"));

            String title = request.queryParams("title");
            int subjectLevelId = Integer.parseInt(request.queryParams("subjectLevelId"));
            String keywords = request.queryParams("keywords");
            String content = request.queryParams("content");
            String additionalContent = request.queryParams("additionalContent");
            String summaryAndGoals = request.queryParams("summaryAndGoals");
            Subject subject;
            if ("edit".equals(action)) {
                subject = dbProvider.getSubject(subjectId);
                subject.setAdditionalContent(additionalContent);
                subject.setContent(content);
                subject.setKeywords(keywords);
                subject.setTitle(title);
                subject.setSubjectLevelId(subjectLevelId);
                subject.setSummaryAndGoals(summaryAndGoals);
                createDirIfNotExists(subject.getUrl(), title);

                dbProvider.updateSubject(subject);
            }
            if ("add".equals(action)) {
                Subject parentSubject = dbProvider.getSubject(subjectId);
                SubjectPosition subPosition = SubjectPosition.getById(parentSubject.getSubjectPositionId()).getSubPosition();
                subject = new Subject(0, title, keywords, null, subjectId, parentSubjectId, SubjectLevel.OSNOVNO.getId(), 0, content, additionalContent, summaryAndGoals, subPosition.getId(), subPosition);
                if (subject.getSubjectPosition().getId() < 3) {
                    String titleReplaced = title.replaceAll(" ", "").replaceAll("[^\\x00-\\x7F]", "");
                    String filename = titleReplaced.substring(0, titleReplaced.length() > 10 ? 10 : titleReplaced.length()).toLowerCase();
                    String filePath = parentSubject.getUrl().concat("/").concat(filename);
                    createDirIfNotExists(filePath, title);
                    subject.setUrl(filePath);
                } else {
                    subject.setUrl(parentSubject.getUrl());
                }
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

    private static void createDirIfNotExists(String subjectDirectory, String title) {
        String filePath = AppConfiguration.getInstance().getExternalLocation().concat(subjectDirectory);
        File dir = new File(filePath);
        if (!dir.exists()) {
            try {
                boolean createdDirs = dir.mkdirs();
                LOGGER.info("directory: {} - created: {}", dir.getPath(), createdDirs);
            } catch (SecurityException e) {
                LOGGER.error("error creating dir: {} for title: {}", dir.getPath(), title, e);
            }
        } else {
            LOGGER.info("File already exists.");
        }
    }
}
