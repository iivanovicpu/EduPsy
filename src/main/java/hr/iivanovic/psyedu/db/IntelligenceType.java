package hr.iivanovic.psyedu.db;

import java.util.List;

import lombok.Data;

/**
 * @author iivanovic
 * @date 09.04.17.
 */
@Data
public class IntelligenceType {
    private int id;
    private String Name;
    private String code;
    private List<AdaptiveRules> adaptiveRules;
}
