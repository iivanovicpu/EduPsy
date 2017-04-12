package hr.iivanovic.psyedu.db;

import java.util.List;

public interface Model {

    void createUser(String username, String password, String firstName, String lastName, String email, String status);

    void createSubject(String title, String keywords, String url);

    List<User> getAllUsers();

    User getUserByUsername(String username);

    List<Subject> getAllSubjects();

    Subject getSubject(long id);

    void createQuestion(Question question);

    List<Question> getAllQuestionsForSubjectAndTitle(int subjectId, String titleId);

    List<AdaptiveRule> getAllAdaptiveRules();

    int nextIdx(String tag);

    void save(User user);

    void logLearningStatus(int studentId, int subjectId, String titleId, int statusId);

    LearningLog getLearningLogStatus(int studentId, int subjectId, String titleId);

    List<User> getAllStudents();

    void updateStudentIntelligenceType(int id, int intelligenceTypeId);

    User getUserById(int studentId);

    void updateStudentLearningStylePollResult(int id, int aktivni, int reflektivni, int opazajni, int intuitivni, int vizualni, int verbalni, int sekvencijalni, int globalni);

    List<IntelligenceType> getAllIntelligenceTypes();

    List<LearningStyle> getAllLearningStyles();

}