package hr.iivanovic.psyedu.controllers;

import hr.iivanovic.psyedu.AppConfiguration;
import hr.iivanovic.psyedu.db.*;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;

public class UploadAnswerController extends AbstractController {

    public static Route uploadFile = (Request request, Response response) -> {
        String location = "img";          // the directory location where files will be stored
        long maxFileSize = 100000000;       // the maximum size allowed for uploaded files
        long maxRequestSize = 100000000;    // the maximum size allowed for multipart/form-data requests
        int fileSizeThreshold = 1024;       // the size threshold after which files will be written to disk
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
        request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
        Collection<Part> parts = request.raw().getParts();

        int questionId = Integer.parseInt(request.queryParams("questionId"));
        int studentId = Integer.parseInt(request.queryParams("studentId"));
        int subjectId = Integer.parseInt(request.queryParams("subjectId"));
            for (Part part : parts) {
                String fileNameOrig = part.getSubmittedFileName();
                String fileName = null != fileNameOrig ? fileNameOrig.replaceAll(" ", "") : null;
                String appExternalLocation = AppConfiguration.getInstance().getExternalLocation();
                String fullPath = appExternalLocation + "/answers" + "/q-" + questionId + "-s-" + studentId + "-" + fileName;
                java.nio.file.Path out = Paths.get(fullPath);
                try (final InputStream in = part.getInputStream()) {
                    if (null != fileName) {
                        Files.copy(in, out);
                        part.delete();
                    }
                } catch (FileAlreadyExistsException ignored) {
                    // todo: uključiti u validaciju
                    // if filename already exists upload will not be executed
                }
            }
        User student = LoginController.getCurrentUser(request);
        List<Question> questions = dbProvider.getAllQuestionsForSubject(subjectId, student.groupQuestions());

        return ViewUtil.render(request, ExamController.createExamModel(request,subjectId, student, questions, null ), ExamController.EXAM);
    };
}
