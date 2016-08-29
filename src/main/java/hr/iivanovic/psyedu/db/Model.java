package hr.iivanovic.psyedu.db;

import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createUser(String username, String password, String firstName, String lastName, String email);

    UUID createSubject(String title, String keywords, String url);

    List<User> getAllUsers();

    List<Subject> getAllSubjects();

    Subject getSubject(long id);
}