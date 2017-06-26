package hr.iivanovic.psyedu.db;

import java.util.LinkedList;
import java.util.List;

import hr.iivanovic.psyedu.controllers.LearningStyle;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author iivanovic
 * @date 28.08.16.
 */
@Data
@Slf4j
public class User {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String color;
    private int learningStyleId;
    private boolean completedLearningStylePoll;
    private boolean completedIntelligencePoll;

    private int lsPointsActive;
    private int lsPointsReflective;
    private int lsPointsVisual;
    private int lsPointsVerbal;
    private int lsPointsSequential;
    private int lsPointsGlobal;
    private int lsPointsIntuitive;
    private int lsPointsSensor;

    private int intelligencePointsVerbal;
    private int intelligencePointsNotVerbal;
    private int intelligencePointsMathLogic;

    private List<LearningStyle> learningStyles;
    private IntelligenceType intelligenceType;

    private boolean debug;
    private List<AdaptiveRule> debugRules;
    private List<AdaptiveRule> userRules;

    public void fillUserAdaptiveRules() {
        userRules = new LinkedList<>();
        learningStyles.forEach(learningStyle -> userRules.addAll(LearningStyleRule.findRulesByLearningStyle(learningStyle)));
        userRules.addAll(IntelligenceTypeRule.findRulesByIntelligenceType(intelligenceType));
    }

    public void resolveIntelligenceType(){
        if(!completedIntelligencePoll){
            intelligenceType = IntelligenceType.O;
        } else if (intelligencePointsVerbal > intelligencePointsNotVerbal && intelligencePointsVerbal > intelligencePointsMathLogic){
            intelligenceType = IntelligenceType.V;
        } else if (intelligencePointsNotVerbal > intelligencePointsVerbal && intelligencePointsNotVerbal > intelligencePointsMathLogic){
            intelligenceType = IntelligenceType.NV;
        } else if (intelligencePointsMathLogic > intelligencePointsVerbal && intelligencePointsMathLogic > intelligencePointsNotVerbal) {
            intelligenceType = IntelligenceType.ML;
        }
    }

    public List<AdaptiveRule> getUserRules() {
        return userRules;
    }

    public void removeAdaptiveRule(AdaptiveRule adaptiveRule) {
        userRules.removeIf(adaptiveRule1 -> adaptiveRule1.equals(adaptiveRule));
        log.info("debug: adaptive rule {} removed from user rules", adaptiveRule);
    }

    public void addAdaptiveRule(AdaptiveRule adaptiveRule) {
        if (!userRules.contains(adaptiveRule)) {
            userRules.add(adaptiveRule);
        }
        log.info("debug: adaptive rule {} added to user rules", adaptiveRule);
    }

    public boolean hasImmutableIntelligenceTypeRule(){
        return userRules.stream().anyMatch(AdaptiveRule.P14_IMMUTABLE_LEARNING_STYLE_VALUES::equals);
    }

    public boolean groupQuestions(){
        return getUserRules().stream().anyMatch(AdaptiveRule.P7_QUESTIONS_GROUPING::equals);
    }

    public boolean essayQuestions(){
        return getUserRules().stream().anyMatch(AdaptiveRule.P9_QUESTIONS_HOW_WHAT_WHY::equals);
    }

    public boolean shortQuestions(){
        return getUserRules().stream().anyMatch(AdaptiveRule.P11_SHORT_QUESTIONS::equals);
    }

}
