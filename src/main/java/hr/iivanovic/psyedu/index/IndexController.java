package hr.iivanovic.psyedu.index;

import java.util.HashMap;
import java.util.Map;

import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;
import static hr.iivanovic.psyedu.Application.userDao;
import static hr.iivanovic.psyedu.Application.bookDao;

public class IndexController {
    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userDao.getAllUserNames());
        model.put("book", bookDao.getRandomBook());
        return ViewUtil.render(request, model, Path.Template.INDEX);
    };
}
