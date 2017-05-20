package hr.iivanovic.psyedu.controllers.view;

import static hr.iivanovic.psyedu.controllers.AbstractController.dbProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hr.iivanovic.psyedu.controllers.LearningStyle;
import hr.iivanovic.psyedu.db.AdaptiveRule;
import hr.iivanovic.psyedu.db.IntelligenceType;
import hr.iivanovic.psyedu.db.IntelligenceTypeRule;
import hr.iivanovic.psyedu.db.LearningLog;
import hr.iivanovic.psyedu.db.LearningStyleRule;
import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.db.SubjectPosition;
import hr.iivanovic.psyedu.db.TitleLearningStatus;
import hr.iivanovic.psyedu.db.User;
import lombok.Data;

/**
 * @author iivanovic
 * @date 03.05.17.
 */
@Data
public class SubjectView extends Subject {

    private static int SEQ_NAVIGATION_CONDITION = TitleLearningStatus.FINISHED_EXAM.getId();

    public SubjectView(Subject sub, User student) {
        super(sub.getId(), sub.getTitle(), sub.getKeywords(), sub.getUrl(), sub.getSubjectId(), sub.getParentSubjectId(), sub.getSubjectLevelId(), sub.getOrder(), sub.getContent(), sub.getAdditionalContent(), null, sub.getSubjectPositionId(), sub.getSubjectPosition());
        this.student = student;
    }

    private TitleLearningStatus titleLearningStatus;

    private int nextSubjectId;

    private User student;

    public List<AdaptiveRule> getAdaptiveRules() {
        return student.getUserRules();
    }

    public TitleLearningStatus getLearningStatus() {
        LearningLog learningLogStatus = dbProvider.getLearningLogStatus(student.getId(), this.getId());
        return TitleLearningStatus.getById(null != learningLogStatus ? learningLogStatus.getStatusId() : TitleLearningStatus.NONE.getId());
    }

    public boolean isSequenceNavigation() {
        return getAdaptiveRules().stream().anyMatch(adaptiveRule -> adaptiveRule.equals(AdaptiveRule.P5_SEQUENTIAL_NAVIGATION));
    }

    public String getHighlightJavasript(){
        if(isHighlightNeeded()){
            StringBuilder sb = new StringBuilder();
            sb.append("<script type=\"application/javascript\">");
            for (String keyword : keywords()) {
                sb.append("$(\"#txt\").highlight(\"").append(keyword).append("\");");
                if(showAdditionalContent()){
                    sb.append("$(\"#atxt\").highlight(\"").append(keyword).append("\");");
                }
            }
            sb.append("</script>");
            return sb.toString();
        }
        return "";
    }

    public boolean isHighlightNeeded() {
        return getAdaptiveRules().stream().anyMatch(adaptiveRule -> adaptiveRule.equals(AdaptiveRule.P6_KEYWORDS_HIGHLIGHTING));
    }

    public boolean showAdditionalContent(){
        return getAdaptiveRules().stream().anyMatch(adaptiveRule -> adaptiveRule.equals(AdaptiveRule.P1_SHOW_ADVANCED_SUBJECTS));
    }

    private static List<SubjectView> createSubjectViews(List<Subject> subjects, User currentUser) {
        List<SubjectView> subjectViews = new LinkedList<>();
        for (Subject subject : subjects) {
            subjectViews.add(new SubjectView(subject, currentUser));
        }
        return subjectViews;
    }

    public boolean isLinkAllowed(){
        if(isSequenceNavigation()){
            return getLearningStatus().isFinished() || SubjectPosition.PREDMET.equals(getSubjectPosition());
        }
        return true;
    }

    public String[] keywords() {
        return null != super.getKeywords() ? super.getKeywords().split(",") : new String[]{};
    }

    public String createSidebarNavigation() {
        List<SubjectView> subjectViews = getSubjectViewsForSameParent();

        StringBuilder stringBuilder = new StringBuilder();

        for (SubjectView subjView : subjectViews) {
            if (!this.isSequenceNavigation()) {
                stringBuilder
                        .append("<p><a href='/onetitlechild/")
                        .append(subjView.getId())
                        .append("/'>")
                        .append(subjView.getTitle())
                        .append("</a></p>");
            } else {
                if (subjView.getId() == getNextChildId() || subjView.getId() == getNextParentId() || subjView.getLearningStatus().getId() >= SEQ_NAVIGATION_CONDITION) {
                    stringBuilder
                            .append("<p><a href='/onetitlechild/")
                            .append(subjView.getId())
                            .append("/'>")
                            .append(subjView.getTitle())
                            .append("</a></p>");
                } else {
                    stringBuilder
                            .append("<p>")
                            .append(subjView.getTitle())
                            .append("</p>");
                }
            }
        }
        return stringBuilder.toString();
    }

    public int getNextParentId() {
        int nextParentId;
        SubjectView nextParentSubject = getNextParent();
        nextParentId = null != nextParentSubject && this.getLearningStatus().getId() >= SEQ_NAVIGATION_CONDITION ? nextParentSubject.getId() : 0;
        return nextParentId;
    }

    private SubjectView getNextParent() {
        Map<Integer, List<SubjectView>> groupingByParentSubjectId = getSubjectViewsForSameParent().stream().collect(Collectors.groupingBy(Subject::getParentSubjectId));
        List<SubjectView> parents = groupingByParentSubjectId.get(this.getParentSubjectId());
        return null != parents ? parents.stream().filter(s -> s.getId() > this.getId()).findFirst().orElse(null) : null;
    }

    public int getNextChildId() {
        int nextChildId;
        SubjectView childSubjectView = getNextChild();
        nextChildId = null != childSubjectView && this.getLearningStatus().getId() >= SEQ_NAVIGATION_CONDITION ? childSubjectView.getId() : 0;
        return nextChildId;
    }

    private SubjectView getNextChild() {
        Map<Integer, List<SubjectView>> groupingBySubjectId = getSubjectViewsForSameParent().stream().filter(s -> null != s.getSubjectId()).collect(Collectors.groupingBy(Subject::getSubjectId));
        List<SubjectView> childs = groupingBySubjectId.get(this.getId());
        return null != childs ? childs.stream().findFirst().orElse(null) : null;
    }

    public List<SubjectView> getSubjectViewsForSameParent() {
        return createSubjectViews(dbProvider.getSubjectsByParentSubjectId(this.getParentSubjectId()), student);
    }
}
