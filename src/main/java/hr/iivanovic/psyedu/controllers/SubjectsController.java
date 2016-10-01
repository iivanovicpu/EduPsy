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
import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.html.HtmlParser;
import hr.iivanovic.psyedu.html.TitleLink;
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
public class SubjectsController extends AbstractController {

    private static HtmlParser htmlParser = HtmlParser.getInstance();

    public static Route fetchAllSubjects = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("subjects", dbProvider.getAllSubjects());
            model.put("editAllowed", LoginController.isEditAllowed(request));
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ALL);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getAllSubjects());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneSubject = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        long id = Long.parseLong(request.params("id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = dbProvider.getSubject(id);
            model.put("subject", subject);
            model.put("successmsg", "");
            boolean isStudent = !LoginController.isEditAllowed(request);
            model.put("isStudent", isStudent);
            if(isStudent){
                File file = new File(AppConfiguration.getInstance().getExternalLocation() + subject.getUrl());
                List<TitleLink> titleLinks = htmlParser.getAllSubjectsLinks(file, subject.getUrl(), subject.getId());
                model.put("titles", titleLinks);
            } else {
                model.put("titles", new LinkedList<String>());
            }
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ONE);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getSubject(id));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneTitle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        String id = request.params("id");
        long subjectIdid = Long.parseLong(request.params("subjectid"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = dbProvider.getSubject(subjectIdid);
            String content = htmlParser.getOneTitleContent(AppConfiguration.getInstance().getExternalLocation() + subject.getUrl().substring(1,subject.getUrl().length()), id);
            model.put("content",content);
            return ViewUtil.render(request, model, Path.Template.SUBJECT_ONE_TITLE);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneSubjectEdit = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        long id = Long.parseLong(request.params(":id"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Subject subject = dbProvider.getSubject(id);
            model.put("subject", subject);
            model.put("editAllowed", LoginController.isEditAllowed(request));
            return ViewUtil.render(request, model, Path.Template.SUBJECTS_ONE_EDIT);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dbProvider.getSubject(id));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

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

            System.out.println(document);
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

    public static Route submitAddedSubject = (request, response) -> {
        String title = request.queryParams("title");
        String keywords = request.queryParams("keywords");
        HashMap<String, Object> model = new HashMap<>();
        System.out.println(title + " " + keywords);
        String validationMsg = validateSubject(title);
        if (!StringUtils.isEmpty(validationMsg)) {
            model.put("validation", validationMsg);
            model.put("editAllowed", LoginController.isEditAllowed(request));
            return ViewUtil.render(request,model, Path.Template.SUBJECT_ADD);
        }
        String titleReplaced = title.replaceAll(" ", "");
        String filename = titleReplaced.substring(0,titleReplaced.length() > 10 ? 10 : titleReplaced.length()).toLowerCase().concat(".html");
        String filePath = AppConfiguration.getInstance().getExternalLocation().concat("materijali/").concat(filename);
        createFileIfNotExists(filePath, title);

        dbProvider.createSubject(title,keywords,"/materijali/".concat(filename));

        // todo: napuni model s tim subjectom i renderiraj stranicu za edit subject-a
        response.redirect(Path.Web.getSUBJECTS());

        if (clientAcceptsHtml(request)) {
//            HashMap<String, Object> model = new HashMap<>();
//            Subject subject = new Subject();
//            model.put("subject", subject);
//            model.put("editAllowed", LoginController.isEditAllowed(request));
        }
        return ViewUtil.notAcceptable.handle(request,response);
    };

    private static void createFileIfNotExists(String filePath, String title){
        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                System.out.println("File is created!");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("<h1>" + title + "</h1>");
                fileWriter.flush();
                fileWriter.close();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String validateSubject(String title) {
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isEmpty(title)){
            sb.append("naslov je obavezan podatak\n");
        } else {
            for (Subject subject : dbProvider.getAllSubjects()) {
                if (subject.getTitle().equals(title)) {
                    sb.append("naslov:\" ").append(title).append("\" već postoji u bazi!");
                }
            }
        }
        return sb.toString();
    }

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
            is.close();
            os.close();
        }
    }

    public static String now() {
        String validPatterns5 = "dd.MM.yyyy-HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(validPatterns5);
        return LocalDateTime.now().format(formatter);
    }


}
