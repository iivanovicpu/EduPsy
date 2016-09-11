package hr.iivanovic.psyedu.index;

import java.util.HashMap;
import java.util.Map;

import hr.iivanovic.psyedu.controllers.AbstractController;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

public class IndexController extends AbstractController {
    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("users", dbProvider.getAllUsers());
        return ViewUtil.render(request, model, Path.Template.INDEX);
    };
}
