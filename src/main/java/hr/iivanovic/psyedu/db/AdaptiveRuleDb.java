package hr.iivanovic.psyedu.db;

import hr.iivanovic.psyedu.controllers.AdaptiveRuleTypes;
import hr.iivanovic.psyedu.controllers.LearningStyle;
import lombok.Data;

/**
 * @author iivanovic
 * @date 12.10.16.
 */
@Data
@Deprecated
public class AdaptiveRuleDb {
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
        return IntelligenceType.getById(this.getIntelligenceTypeId()).getDescription();
    }

    public String getStyleDescription() {
        return LearningStyle.getById(this.getLearningStyleId()).getDescription();
    }

    public String getRuleDescription() {
        return AdaptiveRuleTypes.getById(this.getRuleId()).getDescription();
    }
}
