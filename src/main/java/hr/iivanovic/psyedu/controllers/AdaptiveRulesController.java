package hr.iivanovic.psyedu.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.iivanovic.psyedu.db.AdaptiveRules;
import hr.iivanovic.psyedu.db.IntelligenceType;
import hr.iivanovic.psyedu.db.LearningStyle;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 11.10.16.
 */
public class AdaptiveRulesController extends AbstractController {
    public static Route fetchAllRulesForAdmin = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (LoginController.isEditAllowed(request)) {

            Map<String, Object> model = createModel();
            return ViewUtil.render(request, model, Path.Template.ADAPTIVE_RULES_ADMIN);

        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static Map<String, Object> createModel() {
        List<IntelligenceType> allIntelligenceTypes = dbProvider.getAllIntelligenceTypes();
        List<LearningStyle> allLearningStyles = dbProvider.getAllLearningStyles();

        Map<String, Object> model = new HashMap<>();
        model.put("validation", false);
        model.put("styles", allLearningStyles);
        model.put("intelligences", allIntelligenceTypes);
        model.put("rules", AdaptiveRules.values());
        model.put("adaptiverules", dbProvider.getAllAdaptiveRules());
        return model;
    }

}
