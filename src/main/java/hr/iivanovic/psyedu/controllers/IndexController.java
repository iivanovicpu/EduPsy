package hr.iivanovic.psyedu.controllers;

import java.util.HashMap;
import java.util.Map;

import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

public class IndexController extends AbstractController {
    private final static String INDEX = "/velocity/index/index.vm";

    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("users", dbProvider.getAllUsers());
        return ViewUtil.render(request, model, INDEX);
    };
}
