package hr.iivanovic.psyedu.db;

import java.util.Date;
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
//        this.sql2o = DbUtil.getH2DataSource();
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
    public void createSubject(String title, String keywords, String url) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("insert into subjects(title, keywords, url) VALUES ( :title, :keywords, :url)")
                    .addParameter("title", title)
                    .addParameter("keywords", keywords)
                    .addParameter("url", url)
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
                    .executeAndFetch(Subject.class);
            return subjects;
        }
    }

    @Override
    public Subject getSubject(long id) {
        try (Connection conn = sql2o.open()) {
            Subject subject = conn.createQuery("select * from subjects where id=:id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Subject.class);

            return subject;
        }
    }

    @Override
    public void createQuestion(Question question) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("INSERT INTO questions (subjectId, titleId, question, answers, points) VALUES (:subjectId, :titleId, :question, :answers, :points);")
                    .addParameter("subjectId", question.getSubjectId())
                    .addParameter("titleId", question.getTitleId())
                    .addParameter("question", question.getQuestion())
                    .addParameter("answers", question.getAnswers())
                    .addParameter("points", question.getPoints())
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> getAllQuestionsForSubjectAndTitle(int subjectId, String titleId) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from questions where subjectId=:subjectId and titleId=:titleId")
                    .addParameter("subjectId", subjectId)
                    .addParameter("titleId", titleId)
                    .executeAndFetch(Question.class);
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
    public void logLearningStatus(int studentId, int subjectId, String titleId, int statusId) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("insert into LEARNING_LOG (studentId, subjectId, titleId, date, statusId ) values (:studentId, :subjectId, :titleId, :date, :statusId);")
                    .addParameter("studentId", studentId)
                    .addParameter("subjectId", subjectId)
                    .addParameter("titleId", titleId)
                    .addParameter("date", new Date())
                    .addParameter("statusId", statusId)
                    .executeUpdate();
        }
    }

    @Override
    public LearningLog getLearningLogStatus(int studentId, int subjectId, String titleId) {
        try (Connection conn = sql2o.open()) {
            LearningLog learning = conn.createQuery("select * from LEARNING_LOG where studentId = :studentId and subjectId = :subjectId and titleId = :titleId order by statusId desc;")
                    .addParameter("studentId", studentId)
                    .addParameter("subjectId", subjectId)
                    .addParameter("titleId", titleId)
                    .executeAndFetchFirst(LearningLog.class);
            return learning;
        }
    }
}