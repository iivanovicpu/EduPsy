package hr.iivanovic.psyedu.db;

import java.util.List;

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
        this.sql2o = DbUtil.getH2DataSource();
    }

    public void clearRecordsForReinit() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("delete from subject").executeUpdate();
            conn.createQuery("delete from user").executeUpdate();
            conn.createQuery("delete from question").executeUpdate();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSubject(String title, String keywords, String url) {
        try (Connection conn = sql2o.open()) {
            Integer id = (Integer) conn.createQuery("insert into subject(title, keywords, url) VALUES ( :title, :keywords, :url)")
                    .addParameter("title", title)
                    .addParameter("keywords", keywords)
                    .addParameter("url", url)
                    .executeUpdate().getKey();
            System.out.println("id: " + id);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void createUser(String username, String password, String firstName, String lastName, String email, String status) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("insert into user(username, password, firstName, lastNAme, email, status) VALUES (:username, :password, :firstname, :lastname, :email, :status)")
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("firstname", firstName)
                    .addParameter("lastname", lastName)
                    .addParameter("email", email)
                    .addParameter("status", status)
                    .executeUpdate();
            conn.commit();
        }

    }

    @Override
    public List<User> getAllUsers() {
        try (Connection conn = sql2o.open()) {
            List<User> users = conn.createQuery("select * from user")
                    .executeAndFetch(User.class);
            return users;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try (Connection conn = sql2o.open()) {
            User user = conn.createQuery("select * from user where username=:username")
                    .addParameter("username", username)
                    .executeAndFetchFirst(User.class);

            return user;
        }
    }

    @Override
    public List<Subject> getAllSubjects() {
        try (Connection conn = sql2o.open()) {
            List<Subject> subjects = conn.createQuery("select * from subject")
                    .executeAndFetch(Subject.class);
            return subjects;
        }
    }

    @Override
    public Subject getSubject(long id) {
        try (Connection conn = sql2o.open()) {
            Subject subject = conn.createQuery("select * from subject where id=:id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Subject.class);

            return subject;
        }
    }

    @Override
    public void createQuestion(Question question) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("INSERT INTO question (subjectId, titleId, question, answers, points) VALUES (:subjectId, :titleId, :question, :answers, :points);")
                    .addParameter("subjectId", question.getSubjectId())
                    .addParameter("titleId", question.getTitleId())
                    .addParameter("question", question.getQuestion())
                    .addParameter("answers", question.getAnswers())
                    .addParameter("points", question.getPoints())
                    .executeUpdate();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> getAllQuestionsForSubjectAndTitle(int subjectId, String titleId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from question where subjectId=:subjectId and titleId=:titleId")
                    .addParameter("subjectId", subjectId)
                    .addParameter("titleId", titleId)
                    .executeAndFetch(Question.class);
        }
    }


}