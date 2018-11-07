package hr.iivanovic.psyedu.db;

import java.util.List;

public interface Model {

    void createUser(String username, String password, String firstName, String lastName, String email, String status);

    void createSubject(String title, String keywords, String url, SubjectLevel subjectLevel, int subjectPositionId);

    void createSubSubject(Subject subject);

    List<User> getAllUsers();

    String getRandomMotivationalMessage();

    User getUserByUsername(String username);

    List<Subject> getAllSubjects();

    Subject getSubject(long id);

    void createStudentScore(int subjectId, int studentId, double result, boolean success);

    void updateStudentScore(int subjectId, int studentId, double result, boolean success);

    boolean studentScoreExists(int subjectId, int studentId);

    void createQuestion(Question question);

    void deleteQuestion(int questionId);

    List<Question> getAllQuestionsForSubject(int subjectId, boolean grouped);

    Question getQuestionById(int questionId);

    List<ExternalLink> getAllExternalLinksBySubjectId(int subjectId);

    void createExternalLink(ExternalLink externalLink);

    void deleteExternalLink(int id);

    List<AdaptiveRuleDb> getAllAdaptiveRules();

    int nextIdx(String tag);

    void save(User user);

    void logLearningStatus(int studentId, int subjectId, int statusId);

    LearningLog getLearningLogStatus(int studentId, int subjectId);

    List<User> getAllStudents();

    void updateStudentIntelligenceTypePoints(int userId, int intelligencePointsVerbal, int intelligencePointsNotVerbal, int intelligencePointsMathLogic);

    void decreaseIntelligenceTypePoints(int userId, IntelligenceType intelligenceType);

    User getUserById(int studentId);

    void updateStudentLearningStylePollResult(int id, int aktivni, int reflektivni, int opazajni, int intuitivni, int vizualni, int verbalni, int sekvencijalni, int globalni);

    List<IntelligenceTypeDb> getAllIntelligenceTypes();

    List<LearningStyleDb> getAllLearningStyles();

    List<AdaptiveRule> getIntelligenceTypeRules(int intelligenceTypeId);

    List<Subject> getSubjectsByParentSubjectId(int parentSubjectId);

    void updateSubject(Subject subject);

    List<Subject> getAllParentSubjects();

    List<AdaptiveRule> getLearningStyleRules(int learningStyleId);
}