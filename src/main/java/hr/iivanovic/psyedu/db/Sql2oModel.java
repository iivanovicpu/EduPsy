package hr.iivanovic.psyedu.db;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import hr.iivanovic.psyedu.util.DbUtil;

public class Sql2oModel implements Model {

    private static Sql2oModel instance = null;

    private Sql2o sql2o;

    public static synchronized Sql2oModel getInstance() {
        if (null == instance) {
            instance = new Sql2oModel();
        }
        return instance;
    }

    private Sql2oModel() {
        this.sql2o = DbUtil.getPostgreSQLDataSource();
    }

    public void clearRecordsForReinit() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("delete from subjects").executeUpdate();
            conn.createQuery("delete from users").executeUpdate();
            conn.createQuery("delete from questions").executeUpdate();
            conn.createQuery("delete from learning_log").executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSubject(String title, String keywords, String url, SubjectLevel subjectLevel, int subjectPositionId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("insert into subjects(title, keywords, url, subject_level_id, content, additional_content, subject_position_id) VALUES ( :title, :keywords, :url, :levelid, '<html></html>','<html></html>', :subjectPositionId)")
                    .addParameter("title", title)
                    .addParameter("keywords", keywords)
                    .addParameter("url", url)
                    .addParameter("levelid", subjectLevel.getId())
                    .addParameter("subjectPositionId", subjectPositionId)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSubSubject(Subject subject) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("insert into subjects ( title, keywords, url, subject_id, parent_subject_id, subject_level_id, ordinal_number, content, additional_content, summary_goals, subject_position_id) " +
                    "                       VALUES (:title, :keywords, :url, :subjectId, :parentSubjectId, :subjectLevelId, :order, :content, :additionalContent, :summaryAndGoals, :subjectPositionId)")
                    .addParameter("title", subject.getTitle())
                    .addParameter("keywords", subject.getKeywords())
                    .addParameter("url", subject.getUrl())
                    .addParameter("subjectId", subject.getSubjectId())
                    .addParameter("parentSubjectId", subject.getParentSubjectId())
                    .addParameter("subjectLevelId", subject.getSubjectLevelId())
                    .addParameter("order", subject.getOrder())
                    .addParameter("content", subject.getContent())
                    .addParameter("additionalContent", subject.getAdditionalContent())
                    .addParameter("summaryAndGoals", subject.getSummaryAndGoals())
                    .addParameter("subjectPositionId", subject.getSubjectPosition().getId())
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void createUser(String username, String password, String firstName, String lastName, String email, String status) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("insert into users(username, password, firstName, lastNAme, email, status) VALUES (:username, :password, :firstname, :lastname, :email, :status)")
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("firstname", firstName)
                    .addParameter("lastname", lastName)
                    .addParameter("email", email)
                    .addParameter("status", status)
                    .executeUpdate();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Connection conn = sql2o.open()) {
            List<User> users = conn.createQuery("select * from users")
                    .executeAndFetch(User.class);
            return users;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try (Connection conn = sql2o.open()) {
            User user = conn.createQuery("select * from users where username=:username")
                    .addParameter("username", username)
                    .executeAndFetchFirst(User.class);

            return user;
        }
    }

    @Override
    public List<Subject> getAllSubjects() {
        try (Connection conn = sql2o.open()) {
            List<Subject> subjects = conn.createQuery("select * from subjects")
                    .addColumnMapping("subject_id", "subjectId")
                    .addColumnMapping("parent_subject_id", "parentSubjectId")
                    .addColumnMapping("subject_level_id", "subjectLevelId")
                    .addColumnMapping("ordinal_number", "order")
                    .addColumnMapping("additional_content", "additionalContent")
                    .addColumnMapping("subject_position_id", "subjectPositionId")
                    .addColumnMapping("summary_goals", "summaryAndGoals")
                    .executeAndFetch(Subject.class);
            return subjects;
        }
    }

    @Override
    public Subject getSubject(long id) {
        try (Connection conn = sql2o.open()) {
            Subject subject = conn.createQuery("select * from subjects where id=:id")
                    .addParameter("id", id)
                    .addColumnMapping("subject_id", "subjectId")
                    .addColumnMapping("parent_subject_id", "parentSubjectId")
                    .addColumnMapping("subject_level_id", "subjectLevelId")
                    .addColumnMapping("ordinal_number", "order")
                    .addColumnMapping("additional_content", "additionalContent")
                    .addColumnMapping("subject_position_id", "subjectPositionId")
                    .addColumnMapping("summary_goals", "summaryAndGoals")
                    .executeAndFetchFirst(Subject.class);
                if(null == subject.getParentSubjectId())
                    subject.setParentSubjectId(subject.getId());
            return subject;
        }
    }

    @Override
    public void createQuestion(Question question) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("INSERT INTO questions (subjectId, question, questionTypeId, possibleAnswers, correctAnswers, points) VALUES (:subjectId, :question, :questionTypeId, :possibleAnswers, :correctAnswers, :points);")
                    .addParameter("subjectId", question.getSubjectId())
                    .addParameter("question", question.getQuestion())
                    .addParameter("questionTypeId", question.getQuestionTypeId())
                    .addParameter("possibleAnswers", question.getPossibleAnswers())
                    .addParameter("correctAnswers", question.getCorrectAnswers())
                    .addParameter("points", question.getPoints())
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> getAllQuestionsForSubjectAndTitle(int subjectId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from questions where subjectId=:subjectId")
                    .addParameter("subjectId", subjectId)
                    .executeAndFetch(Question.class);
        }
    }

    @Override
    public List<AdaptiveRuleDb> getAllAdaptiveRules() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from adaptive_rules")
                    .executeAndFetch(AdaptiveRuleDb.class);
        }
    }

    @Override
    public int nextIdx(String tag) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select nextval('all_id_seq')")
                    .executeAndFetchFirst(Integer.class);
        }
    }

    @Override
    public void save(User user) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("update users set color = :color where id = :id")
                    .addParameter("color", user.getColor())
                    .addParameter("id", user.getId())
                    .executeUpdate();
        }
    }

    @Override
    public void logLearningStatus(int studentId, int subjectId, int statusId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("insert into LEARNING_LOG (studentId, subjectId, date, statusId ) values (:studentId, :subjectId, :date, :statusId);")
                    .addParameter("studentId", studentId)
                    .addParameter("subjectId", subjectId)
                    .addParameter("date", new Date())
                    .addParameter("statusId", statusId)
                    .executeUpdate();
        }
    }

    @Override
    public LearningLog getLearningLogStatus(int studentId, int subjectId) {
        try (Connection conn = sql2o.open()) {
            LearningLog learning = conn.createQuery("select * from LEARNING_LOG where studentId = :studentId and subjectId = :subjectId order by statusId desc;")
                    .addParameter("studentId", studentId)
                    .addParameter("subjectId", subjectId)
                    .executeAndFetchFirst(LearningLog.class);
            return learning;
        }
    }

    @Override
    public List<User> getAllStudents() {
        return getAllUsers().stream().filter(user -> user.getStatus().equals("STUDENT")).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void updateStudentIntelligenceType(int userId, int intelligenceTypeId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("update users set intelligenceTypeId = :intelligenceTypeId, completedIntelligencePoll = TRUE where id = :id")
                    .addParameter("intelligenceTypeId", intelligenceTypeId)
                    .addParameter("id", userId)
                    .executeUpdate();
        }
    }

    @Override
    public User getUserById(int studentId) {
        return getAllUsers().stream().filter(user -> user.getId() == studentId).findFirst().orElse(null);
    }

    @Override
    public void updateStudentLearningStylePollResult(int id, int aktivni, int reflektivni, int opazajni, int intuitivni, int vizualni, int verbalni, int sekvencijalni, int globalni) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("update users set lsPointsActive = :lsPointsActive, lsPointsReflective = :lsPointsReflective, " +
                    "lsPointsVisual = :lsPointsVisual, lsPointsVerbal = :lsPointsVerbal, " +
                    "lsPointsSequential = :lsPointsSequential, lsPointsGlobal = :lsPointsGlobal, " +
                    "lsPointsIntuitive = :lsPointsIntuitive, lsPointsSensor = :lsPointsSensor, " +
                    "completedLearningStylePoll = TRUE where id = :id")
                    .addParameter("lsPointsActive", aktivni)
                    .addParameter("lsPointsReflective", reflektivni)
                    .addParameter("lsPointsVisual", vizualni)
                    .addParameter("lsPointsVerbal", verbalni)
                    .addParameter("lsPointsSequential", sekvencijalni)
                    .addParameter("lsPointsGlobal", globalni)
                    .addParameter("lsPointsIntuitive", intuitivni)
                    .addParameter("lsPointsSensor", opazajni)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    @Override
    public List<IntelligenceTypeDb> getAllIntelligenceTypes() {
        try (Connection conn = sql2o.open()) {
            List<IntelligenceTypeDb> intelligenceTypeDbs = conn.createQuery("select * from intelligence_types")
                    .executeAndFetch(IntelligenceTypeDb.class);
            for (IntelligenceTypeDb intelligenceTypeDb : intelligenceTypeDbs) {
                intelligenceTypeDb.setAdaptiveRules(getIntelligenceTypeRules(intelligenceTypeDb.getId()));
            }
            return intelligenceTypeDbs;
        }
    }

    @Override
    public List<AdaptiveRule> getIntelligenceTypeRules(int intelligenceTypeId) {
        List<AdaptiveRule> rules = new LinkedList<>();
        String sql = "SELECT adaptive_rule_id FROM intelligence_adaptive_rules where intelligence_type_id = :intelligenceTypeId";
        try (Connection con = sql2o.open()) {
            List<Integer> ruleIds = con.createQuery(sql)
                    .addParameter("intelligenceTypeId", intelligenceTypeId)
                    .executeScalarList(Integer.class);
            for (Integer ruleId : ruleIds) {
                rules.add(AdaptiveRule.getById(ruleId));
            }
            return rules;
        }
    }

    @Override
    public List<Subject> getSubjectsByParentSubjectId(int parentSubjectId) {
        try (Connection conn = sql2o.open()) {
            List<Subject> subjects = conn.createQuery("select * from subjects where id=:id or parent_subject_id=:id")
                    .addParameter("id", parentSubjectId)
                    .addColumnMapping("subject_id", "subjectId")
                    .addColumnMapping("parent_subject_id", "parentSubjectId")
                    .addColumnMapping("subject_level_id", "subjectLevelId")
                    .addColumnMapping("ordinal_number", "order")
                    .addColumnMapping("additional_content", "additionalContent")
                    .addColumnMapping("subject_position_id", "subjectPositionId")
                    .addColumnMapping("summary_goals", "summaryAndGoals")
                    .executeAndFetch(Subject.class);
            for (Subject subject : subjects) {
                if(null == subject.getParentSubjectId())
                    subject.setParentSubjectId(parentSubjectId);
            }
            return subjects.stream().sorted(Comparator.comparing(Subject::getId)).collect(Collectors.toList());
        }
    }

    @Override
    public List<Subject> getAllParentSubjects() {
        try (Connection conn = sql2o.open()) {
            List<Subject> subjects = conn.createQuery("select * from subjects where subject_id is null")
                    .addColumnMapping("subject_id", "subjectId")
                    .addColumnMapping("parent_subject_id", "parentSubjectId")
                    .addColumnMapping("subject_level_id", "subjectLevelId")
                    .addColumnMapping("ordinal_number", "order")
                    .addColumnMapping("additional_content", "additionalContent")
                    .addColumnMapping("subject_position_id", "subjectPositionId")
                    .addColumnMapping("summary_goals", "summaryAndGoals")
                    .executeAndFetch(Subject.class);
            return subjects;
        }

    }

    @Override
    public void updateSubject(Subject subject) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("update subjects set " +
                    "   title = :title, " +
                    "   keywords = :keywords, " +
                    "   url = :url, " +
                    "   subject_id = :subjectId, " +
                    "   parent_subject_id = :parentSubjectId, " +
                    "   subject_level_id = :subjectLevelId, " +
                    "   ordinal_number = :orderNumber, " +
                    "   content = :content, " +
                    "   additional_content = :additionalContent, " +
                    "   summary_goals = :summaryAndGoals " +
                    " where id = :id")
                    .addParameter("id", subject.getId())
                    .addParameter("subjectId", subject.getSubjectId())
                    .addParameter("parentSubjectId", subject.getParentSubjectId())
                    .addParameter("title", subject.getTitle())
                    .addParameter("keywords", subject.getKeywords())
                    .addParameter("content", subject.getContent())
                    .addParameter("additionalContent", subject.getAdditionalContent())
                    .addParameter("summaryAndGoals", subject.getSummaryAndGoals())
                    .addParameter("orderNumber", subject.getOrder())
                    .addParameter("subjectLevelId", subject.getSubjectLevelId())
                    .addParameter("url", subject.getUrl())
                    .executeUpdate();
        }

    }

    @Override
    public List<LearningStyleDb> getAllLearningStyles() {
        try (Connection conn = sql2o.open()) {
            List<LearningStyleDb> learningStyleDbs = conn.createQuery("select * from learning_styles")
                    .executeAndFetch(LearningStyleDb.class);
            for (LearningStyleDb learningStyleDb : learningStyleDbs) {
                learningStyleDb.setAdaptiveRules(getLearningStyleRules(learningStyleDb.getId()));
            }
            return learningStyleDbs;
        }
    }

    @Override
    public List<AdaptiveRule> getLearningStyleRules(int learningStyleId) {
        List<AdaptiveRule> rules = new LinkedList<>();
        String sql = "SELECT adaptive_rule_id FROM learning_styles_adaptive_rules where learning_style_id = :learningStyleId";
        try (Connection con = sql2o.open()) {
            List<Integer> ruleIds = con.createQuery(sql)
                    .addParameter("learningStyleId", learningStyleId)
                    .executeScalarList(Integer.class);
            for (Integer ruleId : ruleIds) {
                rules.add(AdaptiveRule.getById(ruleId));
            }
            return rules;
        }
    }

}