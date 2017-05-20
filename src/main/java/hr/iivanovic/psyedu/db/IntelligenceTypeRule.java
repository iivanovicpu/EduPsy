package hr.iivanovic.psyedu.db;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author iivanovic
 * @date 30.04.17.
 */
public enum IntelligenceTypeRule {

    ML_1(IntelligenceType.ML, AdaptiveRule.P1_SHOW_ADVANCED_SUBJECTS),
    ML_2(IntelligenceType.ML, AdaptiveRule.P2_SHOW_LINKS),
    ML_3(IntelligenceType.ML, AdaptiveRule.P3_TEXT_MAX_1000),
    ML_5(IntelligenceType.ML, AdaptiveRule.P5_SEQUENTIAL_NAVIGATION),
    ML_6(IntelligenceType.ML, AdaptiveRule.P6_KEYWORDS_HIGHLIGHTING),
    ML_12(IntelligenceType.ML, AdaptiveRule.P12_SEQUENTIAL_ANSWER_CHECK),
    NV_2(IntelligenceType.NV, AdaptiveRule.P2_SHOW_LINKS),
    NV_6(IntelligenceType.NV, AdaptiveRule.P6_KEYWORDS_HIGHLIGHTING),
    NV_7(IntelligenceType.NV, AdaptiveRule.P7_QUESTIONS_GROUPING),
    V_2(IntelligenceType.V, AdaptiveRule.P2_SHOW_LINKS),
    V_6(IntelligenceType.V, AdaptiveRule.P6_KEYWORDS_HIGHLIGHTING);

    private final IntelligenceType intelligenceType;

    private final AdaptiveRule adaptiveRule;

    IntelligenceTypeRule(IntelligenceType intelligenceType, AdaptiveRule adaptiveRule) {
        this.intelligenceType = intelligenceType;
        this.adaptiveRule = adaptiveRule;
    }

    public static List<AdaptiveRule> findRulesByIntelligenceType(IntelligenceType intelligenceType) {
        return Stream.of(IntelligenceTypeRule.values())
                .filter(r -> r.intelligenceType.equals(intelligenceType))
                .map(IntelligenceTypeRule::getAdaptiveRule)
                .collect(Collectors.toList());
    }

    public IntelligenceType getIntelligenceType() {
        return intelligenceType;
    }

    public AdaptiveRule getAdaptiveRule() {
        return adaptiveRule;
    }
}
