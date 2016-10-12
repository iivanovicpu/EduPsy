package hr.iivanovic.psyedu.db;

import hr.iivanovic.psyedu.controllers.AdaptiveRuleTypes;
import hr.iivanovic.psyedu.controllers.IntelligenceTypes;
import hr.iivanovic.psyedu.controllers.LearningStyles;
import lombok.Data;

/**
 * @author iivanovic
 * @date 12.10.16.
 */
@Data
public class AdaptiveRule {
    private int id;
    private int learningStyleId;
    private int intelligenceTypeId;
    private int ruleId;
    private String ruleData;
    private String mark;

    private String intelligenceDescription;
    private String styleDescription;
    private String ruleDescription;

    public String getIntelligenceDescription() {
        return IntelligenceTypes.getById(this.getIntelligenceTypeId()).getDescription();
    }

    public String getStyleDescription() {
        return LearningStyles.getById(this.getLearningStyleId()).getDescription();
    }

    public String getRuleDescription() {
        return AdaptiveRuleTypes.getById(this.getRuleId()).getDescription();
    }
}
