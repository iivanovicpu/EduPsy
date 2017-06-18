package hr.iivanovic.psyedu.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.iivanovic.psyedu.db.AdaptiveRule;
import hr.iivanovic.psyedu.db.IntelligenceTypeDb;
import hr.iivanovic.psyedu.db.LearningStyleDb;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 11.10.16.
 */
public class AdaptiveRulesController extends AbstractController {
    private final static String ADAPTIVE_RULES_ADMIN = "/velocity/adaptiverules.vm";

    public static Route fetchAllRulesForAdmin = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (LoginController.isEditAllowed(request)) {

            Map<String, Object> model = createModel();
            return ViewUtil.render(request, model, ADAPTIVE_RULES_ADMIN);

        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static Map<String, Object> createModel() {
        // todo: from enum
        List<IntelligenceTypeDb> allIntelligenceTypeDbs = dbProvider.getAllIntelligenceTypes();
        List<LearningStyleDb> allLearningStyleDbs = dbProvider.getAllLearningStyles();

        Map<String, Object> model = new HashMap<>();
        model.put("validation", false);
        model.put("styles", allLearningStyleDbs);
        model.put("intelligences", allIntelligenceTypeDbs);
        model.put("rules", AdaptiveRule.values());
        model.put("adaptiverules", dbProvider.getAllAdaptiveRules());
        return model;
    }

}
