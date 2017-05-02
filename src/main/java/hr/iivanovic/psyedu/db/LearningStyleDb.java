package hr.iivanovic.psyedu.db;

import java.util.List;

import lombok.Data;

/**
 * @author iivanovic
 * @date 09.04.17.
 */
@Data
@Deprecated
public class LearningStyleDb {
    private int id;
    private String code;
    private String name;
    private List<AdaptiveRule> adaptiveRules;
}
