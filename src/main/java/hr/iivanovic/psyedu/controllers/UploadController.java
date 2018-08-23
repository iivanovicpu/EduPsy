package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;

import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import hr.iivanovic.psyedu.AppConfiguration;
import hr.iivanovic.psyedu.db.ExternalLink;
import hr.iivanovic.psyedu.db.ExternalLinkType;
import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 22.05.17.
 */
public class UploadController extends AbstractController {
    private final static String UPLOAD = "/velocity/upload.vm";

    // todo: validacija
    public static Route uploadFile = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);

        String location = "img";          // the directory location where files will be stored
        long maxFileSize = 100000000;       // the maximum size allowed for uploaded files
        long maxRequestSize = 100000000;    // the maximum size allowed for multipart/form-data requests
        int fileSizeThreshold = 1024;       // the size threshold after which files will be written to disk
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
        request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
        Collection<Part> parts = request.raw().getParts();

        int subjectId = Integer.parseInt(request.queryParams("subjectId"));
        String title = request.queryParams("title");
        int linkTypeId = Integer.parseInt(request.queryParams("linkTypeId"));
        if (linkTypeId == ExternalLinkType.ATTACHMENT.getId()) {

            String subjectUrl = request.queryParams("subjectUrl");
            for (Part part : parts) {
                String fileNameOrig = part.getSubmittedFileName();
                String fileName = null != fileNameOrig ? fileNameOrig.replaceAll(" ", "") : null;
                String appExternalLocation = AppConfiguration.getInstance().getExternalLocation();
                String fullPath = appExternalLocation + subjectUrl.replaceFirst("/", "") + "/" + fileName;
                String url = subjectUrl + "/" + fileName;
                java.nio.file.Path out = Paths.get(fullPath);
                try (final InputStream in = part.getInputStream()) {
                    if (null != fileName) {
                        Files.copy(in, out);
                        part.delete();
                        dbProvider.createExternalLink(new ExternalLink(0, subjectId, title, url, linkTypeId));
                    }
                } catch (FileAlreadyExistsException ignored) {
                    // todo: uključiti u validaciju
                    // if filename already exists upload will not be executed
                }
            }
            parts = null;
        } else if (linkTypeId == ExternalLinkType.URL.getId()) {
            String url = request.queryParams("url");
            dbProvider.createExternalLink(new ExternalLink(0, subjectId, title, url, linkTypeId));
        }
        return ViewUtil.render(request, createUploadModel(subjectId), UPLOAD);
    };

    public static Route deleteExternalLink = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);

        int subjectId = Integer.parseInt(request.queryParams("subjectId"));
        int linkId = Integer.parseInt(request.queryParams("linkId"));
        dbProvider.deleteExternalLink(linkId);
        return ViewUtil.render(request, createUploadModel(subjectId), UPLOAD);
    };

    public static Route uploadForm = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);

        int subjectId = Integer.parseInt(request.params("subjectId"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = createUploadModel(subjectId);
            return ViewUtil.render(request, model, UPLOAD);
        }
        return ViewUtil.notAcceptable.handle(request, response);

    };

    private static HashMap<String, Object> createUploadModel(int subjectId) {
        Subject subject = dbProvider.getSubject(subjectId);
        List<ExternalLink> linksBySubjectId = dbProvider.getAllExternalLinksBySubjectId(subjectId);
        HashMap<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        model.put("links", linksBySubjectId);
        return model;
    }


}