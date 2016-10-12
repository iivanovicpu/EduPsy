package hr.iivanovic.psyedu.db;

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
}
