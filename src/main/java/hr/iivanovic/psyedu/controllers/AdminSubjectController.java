package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsJson;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import hr.iivanovic.psyedu.AppConfiguration;
import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.learning.HtmlParser;
import hr.iivanovic.psyedu.learning.TitleLink;
import hr.iivanovic.psyedu.login.LoginController;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 26.09.16.
 */
public class AdminSubjectController extends AbstractController {
    private static HtmlParser htmlParser = HtmlParser.getInstance();

    public static Route fetchtitlesForAddQuestions = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (!LoginController.isEditAllowed(request)) {
            return ViewUtil.notAcceptable.handle(request, response);
        }
        long id = Long.parseLong(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = dbProvider.getSubject(id);
            model.put("subject", subject);
            model.put("successmsg", "");
            File file = new File(AppConfiguration.getInstance().getExternalLocation() + subject.getUrl());
            List<TitleLink> titleLinks = htmlParser.getAllSubjectsLinks(file, subject.getUrl(), subject.getId());
            model.put("titles", titleLinks);
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ONE_QUESTIONS);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getSubject(id));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneTitleForAddQuestions = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        String id = request.params("id");
        long subjectIdid = Long.parseLong(request.params("subjectid"));
        System.out.println(id + " " + subjectIdid);
        // todo: kreirati listu posotojećih pitanja i staviti u model (na stranici prikazati tablicu pitanja)
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("validation",false);
            model.put("subjectId",subjectIdid);
            model.put("titleId",id);
            Subject subject = dbProvider.getSubject(subjectIdid);
            String content = htmlParser.getOneTitleContent(AppConfiguration.getInstance().getExternalLocation() + subject.getUrl().substring(1,subject.getUrl().length()), id);
            model.put("content",content);
            return ViewUtil.render(request, model, Path.Template.SUBJECT_ONE_TITLE_QUESTIONS);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitQuestion = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        String titleId = request.queryParams("titleId");
        String subjectId = request.queryParams("subjectId");
        long subjectIdid = Long.parseLong(request.params("subjectid"));
        // todo: uzeti sve query parametre iz forme, validirati, ako je ok spremiti u bazu ako nije staviti poruku u validation
        // todo: parser pitanja i odgovora (odgovori se splitaju znakom |)
        System.out.println("posted: " + titleId + " " + subjectIdid);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("validation",false);
            Subject subject = dbProvider.getSubject(subjectIdid);
            String content = htmlParser.getOneTitleContent(AppConfiguration.getInstance().getExternalLocation() + subject.getUrl().substring(1,subject.getUrl().length()), titleId);
            model.put("content",content);
            model.put("subjectId", subjectId);
            model.put("titleId", titleId);
            return ViewUtil.render(request, model, Path.Template.SUBJECT_ONE_TITLE_QUESTIONS);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

}
