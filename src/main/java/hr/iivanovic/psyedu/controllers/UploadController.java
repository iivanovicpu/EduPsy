package hr.iivanovic.psyedu.controllers;

import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import hr.iivanovic.psyedu.AppConfiguration;
import hr.iivanovic.psyedu.db.ExternalLink;
import hr.iivanovic.psyedu.util.Path;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 22.05.17.
 */
public class UploadController extends AbstractController {
    public static Route uploadFile = (Request request, Response response) -> {
        String location = "img";          // the directory location where files will be stored
        long maxFileSize = 100000000;       // the maximum size allowed for uploaded files
        long maxRequestSize = 100000000;    // the maximum size allowed for multipart/form-data requests
        int fileSizeThreshold = 1024;       // the size threshold after which files will be written to disk

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
        request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

        Collection<Part> parts = request.raw().getParts();
        String subjectId = request.queryParams("subjectId");
        String parentSubjectId = request.queryParams("parentSubjectId");
        String title = request.queryParams("title");
        String subjectUrl = request.queryParams("subjectUrl");
        for (Part part : parts) {
            String fileName = part.getSubmittedFileName();
            String url = AppConfiguration.getInstance().getExternalLocation() + subjectUrl + "/" + fileName;
            java.nio.file.Path out = Paths.get(url);
            System.out.println("File: " + fileName);
            try (final InputStream in = part.getInputStream()) {
                if (null != fileName) {
                    Files.copy(in, out);
                    part.delete();
                    dbProvider.createExternalLink(new ExternalLink(0, Integer.parseInt(subjectId), title, url));
                }
            } catch (FileAlreadyExistsException ignored) {
                // if filename already exists upload will not be executed
            }
        }
        parts = null;

        response.redirect(Path.Web.EDIT_SUBJECT_ITEM
                .replaceFirst(":id", subjectId)
                .replaceFirst(":parentId", parentSubjectId)
                .replaceFirst(":action", "edit"));

        return null;
    };
}
