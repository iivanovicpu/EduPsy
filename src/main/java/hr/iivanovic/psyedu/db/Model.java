package hr.iivanovic.psyedu.db;

import java.util.List;
import java.util.UUID;

public interface Model {

    void createUser(String username, String password, String firstName, String lastName, String email, String status);

    void createSubject(String title, String keywords, String url);

    List<User> getAllUsers();

    User getUserByUsername(String username);

    List<Subject> getAllSubjects();

    Subject getSubject(long id);

    void createQuestion(Question question);

    List<Question> getAllQuestionsForSubjectAndTitle(int subjectId, String titleId);
}