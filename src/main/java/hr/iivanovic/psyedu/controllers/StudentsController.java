package hr.iivanovic.psyedu.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.iivanovic.psyedu.db.IntelligenceType;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import lombok.Data;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 10.10.16.
 */
public class StudentsController extends AbstractController {

    public static Route fetchAllStudents = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);

        List<User> students = dbProvider.getAllStudents();
        Map<String, Object> model = new HashMap<>();
        model.put("students", students);
        return ViewUtil.render(request, model, Path.Template.STUDENTS);
    };

    public static Route fetchStudentDetails = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);

        int studentId = Integer.parseInt(request.params("id"));
        User student = dbProvider.getUserById(studentId);
        if (null != student) {
            Map<String, Object> model = new HashMap<>();
            model.put("validation", false);
            model.put("student", student);
            model.put("styles", LearningStyle.values());
            model.put("intelligenceType", IntelligenceType.getById(student.getIntelligenceTypeId()).getDescription());
            return ViewUtil.render(request, model, Path.Template.STUDENT_DETAILS);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route addStudent = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);

        Map<String, Object> model = new HashMap<>();
        model.put("validation", false);
        StudentView student = new StudentView();
        model.put("student", student);
        return ViewUtil.render(request, model, Path.Template.STUDENTS_ADD);
    };

    public static Route submitStudent = (Request request, Response response) -> {
        if (isAuthorized(request, response)) return ViewUtil.notAllowed.handle(request, response);

        String idStr = request.queryParams("id");
        Integer id = null == idStr ? null : Integer.parseInt(idStr);
        String firstName = request.queryParams("firstName");
        String lastName = request.queryParams("lastName");
        String email = request.queryParams("email");
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        String passwordr = request.queryParams("passwordr");
        StudentView studentView = new StudentView(id, firstName, lastName, username, email, password, passwordr);
        String validation = validateStudent(id, firstName, lastName, email, password, passwordr);
        Map<String, Object> model = new HashMap<>();
        boolean notValid = validation.length() > 0;
        model.put("validation", notValid ? validation : false);
        model.put("student", studentView);
        if (!notValid) {
            dbProvider.createUser(studentView.getUsername(), studentView.getPassword(), studentView.getFirstName(), studentView.getLastName(), studentView.getPassword(), studentView.getStatus());
        }
        return ViewUtil.render(request, model, Path.Template.STUDENTS_ADD);
    };

    private static String validateStudent(Integer id, String firstName, String lastName, String email, String password, String passwordr) {
        StringBuilder sb = new StringBuilder();
        if (firstName.isEmpty()) {
            sb.append("ime je obavezan podatak<br>");
        }
        if (lastName.isEmpty()) {
            sb.append("prezime je obavezan podatak<br>");
        }
        if (email.isEmpty()) {
            sb.append("email je obavezan podatak<br>");
        }
        if (password.isEmpty()) {
            sb.append("lozinka je obavezan podatak<br>");
        }
        if (!password.equals(passwordr)) {
            sb.append("lozinke moraju biti iste<br>");
        }

        return sb.toString();
    }

    @Data
    static class StudentView extends User {

        private String passwordr;

        public StudentView(Integer id, String firstName, String lastName, String username, String email, String password, String passwordr) {
            super();
            setId(id);
            setFirstName(firstName);
            setLastName(lastName);
            setPasswordr(passwordr);
            setUsername(username);
            setPassword(password);
            setEmail(email);
            setStatus("STUDENT");
        }

        public StudentView() {
            super();
            setColor("");
            setLastName("");
            setEmail("");
            setUsername("");
            setPassword("");
            setFirstName("");
            setStatus("STUDENT");
            setPasswordr("");
        }
    }
}
