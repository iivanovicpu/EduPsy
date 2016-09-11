package hr.iivanovic.psyedu.db;

import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createUser(long id, String username, String password, String firstName, String lastName, String email);

    void createSubject(String title, String keywords, String url);

    List<User> getAllUsers();

    User getUserByUsername(String username);

    List<Subject> getAllSubjects();

    Subject getSubject(long id);

    void test();
}