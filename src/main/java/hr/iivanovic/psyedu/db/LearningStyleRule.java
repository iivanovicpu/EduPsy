package hr.iivanovic.psyedu.db;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.iivanovic.psyedu.controllers.LearningStyle;

/**
 * @author iivanovic
 * @date 29.04.17.
 */
public enum LearningStyleRule {
    AKT_2(LearningStyle.STYLE_1_AKT, AdaptiveRule.P2_SHOW_LINKS),
    AKT_9(LearningStyle.STYLE_1_AKT, AdaptiveRule.P9_QUESTIONS_HOW_WHAT_WHY),
    REF_5(LearningStyle.STYLE_1_REF, AdaptiveRule.P5_SEQUENTIAL_NAVIGATION),
    REF_10(LearningStyle.STYLE_1_REF, AdaptiveRule.P10_ASK_FOR_SUMMARY),
    VIZ_2(LearningStyle.STYLE_2_VIS, AdaptiveRule.P2_SHOW_LINKS),
    VIZ_6(LearningStyle.STYLE_2_VIS, AdaptiveRule.P6_KEYWORDS_HIGHLIGHTING),
    VIZ_7(LearningStyle.STYLE_2_VIS, AdaptiveRule.P7_QUESTIONS_GROUPING),
    VER_2(LearningStyle.STYLE_2_VER, AdaptiveRule.P2_SHOW_LINKS),
    VER_6(LearningStyle.STYLE_2_VER, AdaptiveRule.P6_KEYWORDS_HIGHLIGHTING),
    SEK_5(LearningStyle.STYLE_3_SEQ, AdaptiveRule.P5_SEQUENTIAL_NAVIGATION),
    SEK_3(LearningStyle.STYLE_3_SEQ, AdaptiveRule.P3_TEXT_MAX_1000),
    GLO_7(LearningStyle.STYLE_3_GLO, AdaptiveRule.P7_QUESTIONS_GROUPING),
    GLO_8(LearningStyle.STYLE_3_GLO, AdaptiveRule.P8_LONGER_TEXT),
    INT_1(LearningStyle.STYLE_4_INT, AdaptiveRule.P1_SHOW_ADVANCED_SUBJECTS),
    INT_9(LearningStyle.STYLE_4_INT, AdaptiveRule.P9_QUESTIONS_HOW_WHAT_WHY),
    SEN_5(LearningStyle.STYLE_4_SEN, AdaptiveRule.P5_SEQUENTIAL_NAVIGATION),
    SEN_11(LearningStyle.STYLE_4_SEN, AdaptiveRule.P11_SHORT_QUESTIONS);

    private final LearningStyle learningStyle;
    private final AdaptiveRule adaptiveRule;

    LearningStyleRule(LearningStyle learningStyle, AdaptiveRule adaptiveRule) {
        this.learningStyle = learningStyle;
        this.adaptiveRule = adaptiveRule;
    }

    public LearningStyle getLearningStyle() {
        return learningStyle;
    }

    public AdaptiveRule getAdaptiveRule() {
        return adaptiveRule;
    }

    public static List<AdaptiveRule> findRulesByLearningStyle(LearningStyle learningStyle) {
        return Stream.of(LearningStyleRule.values())
                .filter(r -> r.learningStyle.equals(learningStyle))
                .map(LearningStyleRule::getAdaptiveRule)
                .collect(Collectors.toList());
    }
}
