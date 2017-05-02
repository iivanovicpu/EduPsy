package hr.iivanovic.psyedu.db;

/**
 * @author iivanovic
 * @date 30.04.17.
 */
public enum IntelligenceTypeRule {

    ML_1(IntelligenceType.ML, AdaptiveRule.SHOW_ADVANCED_SUBJECTS),
    ML_2(IntelligenceType.ML, AdaptiveRule.SHOW_LINKS),
    ML_3(IntelligenceType.ML, AdaptiveRule.TEXT_MAX_1000),
    ML_5(IntelligenceType.ML, AdaptiveRule.SEQUENTIAL_NAVIGATION),
    ML_6(IntelligenceType.ML, AdaptiveRule.KEYWORDS_HIGHLIGHTING),
    ML_12(IntelligenceType.ML, AdaptiveRule.SEQUENTIAL_ANSWER_CHECK),
    NV_2(IntelligenceType.NV, AdaptiveRule.SHOW_LINKS),
    NV_6(IntelligenceType.NV, AdaptiveRule.KEYWORDS_HIGHLIGHTING),
    NV_7(IntelligenceType.NV, AdaptiveRule.QUESTIONS_GROUPING),
    V_2(IntelligenceType.V, AdaptiveRule.SHOW_LINKS),
    V_6(IntelligenceType.V, AdaptiveRule.KEYWORDS_HIGHLIGHTING);

    private final IntelligenceType intelligenceType;

    private final AdaptiveRule adaptiveRule;

    IntelligenceTypeRule(IntelligenceType intelligenceType, AdaptiveRule adaptiveRule) {
        this.intelligenceType = intelligenceType;
        this.adaptiveRule = adaptiveRule;
    }
}
