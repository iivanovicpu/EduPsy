package hr.iivanovic.psyedu.db;

import java.util.List;
import java.util.UUID;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import hr.iivanovic.psyedu.util.RandomUuidGenerator;
import hr.iivanovic.psyedu.util.UuidGenerator;

public class Sql2oModel implements Model {

    private Sql2o sql2o;
    private UuidGenerator uuidGenerator;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;
        uuidGenerator = new RandomUuidGenerator();
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
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public UUID createUser(long id, String username, String password, String firstName, String lastName, String email) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID userUuid = uuidGenerator.generate();
            conn.createQuery("insert into korisnik(korisnik_uuid, korisnicko_ime, lozinka, ime, prezime, email) VALUES (:korisnik_uuid, :korisnicko_ime, :lozinka, :ime, :prezime, :email)")
                    .addParameter("korisnik_uuid", userUuid)
                    .addParameter("korisnicko_ime", username)
                    .addParameter("lozinka", password)
                    .addParameter("ime", firstName)
                    .addParameter("prezime", lastName)
                    .addParameter("email", email)
                    .executeUpdate();
            conn.commit();
            return userUuid;
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
    public void test() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("INSERT INTO subject (title, keywords, url) VALUES ('TEST', 'test','materijali/test.html');")
                    .executeUpdate();
            conn.commit();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}