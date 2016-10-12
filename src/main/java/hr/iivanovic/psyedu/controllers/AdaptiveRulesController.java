package hr.iivanovic.psyedu.controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.iivanovic.psyedu.db.AdaptiveRule;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import lombok.Data;
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
        if(LoginController.isEditAllowed(request)){
            AdaptiveRulesController thisInstance = new AdaptiveRulesController();

            Map<String, Object> model = new HashMap<>();
            model.put("validation", false);
            model.put("styles", LearningStyles.values());
            model.put("intelligences", IntelligenceTypes.values());
            model.put("rules", AdaptiveRuleTypes.values());
            model.put("adaptiverules", dbProvider.getAllAdaptiveRules());
            return ViewUtil.render(request, model, Path.Template.ADAPTIVE_RULES_ADMIN);

        }
        return ViewUtil.notAcceptable.handle(request,response);
    };

    public static Route submitRule = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request,response);
        if(LoginController.isEditAllowed(request)){
            AdaptiveRulesController thisInstance = new AdaptiveRulesController();
            int styleId = Integer.parseInt(request.queryParams("style"));
            int intelligenceId = Integer.parseInt(request.queryParams("intelligence"));
            int ruleId = Integer.parseInt(request.queryParams("rule"));
            String mark = request.queryParams("mark");
            dbProvider.createAdaptiveRule(styleId, intelligenceId, ruleId, mark);
            Map<String, Object> model = new HashMap<>();
            model.put("validation", false);
            model.put("styles", LearningStyles.values());
            model.put("intelligences", IntelligenceTypes.values());
            model.put("rules", AdaptiveRuleTypes.values());
            model.put("adaptiverules", dbProvider.getAllAdaptiveRules());
            return ViewUtil.render(request, model, Path.Template.ADAPTIVE_RULES_ADMIN);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private List<AdaptiveRuleView> createView(List<AdaptiveRule> data){
        List<AdaptiveRuleView> ruleViewList = new LinkedList<>();
        for (AdaptiveRule adaptiveRule : data) {
            ruleViewList.add(new AdaptiveRuleView(adaptiveRule));
        }
        return ruleViewList;
    }

    @Data
    private class AdaptiveRuleView extends AdaptiveRule {
        protected String intelligenceDescription;
        protected String styleDescription;
        protected String ruleDescription;
        public AdaptiveRuleView(AdaptiveRule adaptiveRule) {
            super();
            this.intelligenceDescription = IntelligenceTypes.getById(adaptiveRule.getIntelligenceTypeId()).getDescription();
            this.ruleDescription = AdaptiveRuleTypes.getById(adaptiveRule.getIntelligenceTypeId()).getDescription();
            this.styleDescription = LearningStyles.getById(adaptiveRule.getIntelligenceTypeId()).getDescription();
            setId(adaptiveRule.getId());
            setIntelligenceTypeId(adaptiveRule.getIntelligenceTypeId());
            setMark(adaptiveRule.getMark());
            setLearningStyleId(adaptiveRule.getLearningStyleId());
            setRuleId(adaptiveRule.getRuleId());
            setRuleData(adaptiveRule.getRuleData());
        }
    }
}
