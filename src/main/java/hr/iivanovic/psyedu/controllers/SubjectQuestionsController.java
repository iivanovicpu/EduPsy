package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsJson;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import hr.iivanovic.psyedu.AppConfiguration;
import hr.iivanovic.psyedu.db.Question;
import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.html.HtmlParser;
import hr.iivanovic.psyedu.html.TitleLink;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import lombok.Data;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 26.09.16.
 */
public class SubjectQuestionsController extends AbstractController {

    public static Route fetchOneTitleForAddQuestions = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        int subjectId = Integer.parseInt(request.params("subjectid"));
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = createModel(subjectId);
            List<Question> questions = dbProvider.getAllQuestionsForSubjectAndTitle(subjectId);
            String htmlQuestions = renderQuestions(questions, LoginController.isStudent(request));
            model.put("questions", htmlQuestions);

            return ViewUtil.render(request, model, Path.Template.SUBJECT_ONE_TITLE_QUESTIONS);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static HashMap<String, Object> createModel(int subjectId) {
        Subject subject = dbProvider.getSubject(subjectId);
        HashMap<String, Object> model = new HashMap<>();
        model.put("validation",false);
        model.put("subjectId",subjectId);
        model.put("subject",subject);
        QuestionType[] questionTypes = QuestionType.values();
        model.put("questionTypes", questionTypes);
        return model;
    }

    public static Route submitQuestion = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            int subjectId = Integer.parseInt(request.queryParams("subjectid"));
            ValidationResult validationResult = validatedQuestion(request);

            HashMap<String, Object> model = createModel(subjectId);
            if(validationResult.isValid()){
                model.put("validation", false);
                System.out.println(validationResult);
                dbProvider.createQuestion(validationResult.question);
            }
            model.put("validation", validationResult.getValidationMessage());
            List<Question> questions = dbProvider.getAllQuestionsForSubjectAndTitle(subjectId);
            String htmlQuestions = renderQuestions(questions, LoginController.isStudent(request));
            model.put("questions", htmlQuestions);
            return ViewUtil.render(request, model, Path.Template.SUBJECT_ONE_TITLE_QUESTIONS);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route deleteQuestion = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            int subjectId = Integer.parseInt(request.queryParams("subjectid"));
            int questionId = Integer.parseInt(request.queryParams("questionid"));
            dbProvider.deleteQuestion(questionId);

            HashMap<String, Object> model = createModel(subjectId);
            model.put("validation", "zapis uspješno obrisan!");
            List<Question> questions = dbProvider.getAllQuestionsForSubjectAndTitle(subjectId);
            String htmlQuestions = renderQuestions(questions, LoginController.isStudent(request));
            model.put("questions", htmlQuestions);
            return ViewUtil.render(request, model, Path.Template.SUBJECT_ONE_TITLE_QUESTIONS);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static ValidationResult validatedQuestion(Request request) {
        ValidationResult result = new ValidationResult();
        StringBuilder sb = new StringBuilder();
        int subjectid;
        int points;
        String possibleAnswers = null;
        String correctAnswers = null;
        try {
            subjectid = Integer.parseInt(request.queryParams("subjectid"));
            if(subjectid == 0){
                sb.append("header data corrupt - SubjectId (0)");
                result.setValid(false);
            }
        } catch (NumberFormatException e){
            sb.append("header data corrupt - subjectId +(" ).append(request.queryParams("subjectId")).append(")");
            result.setValid(false);
            throw e;
        }
        String question = request.queryParams("question");
        if(null == question || question.isEmpty()){
            sb.append("Pitanje - obavezan unos<br>");
            result.setValid(false);
        }
        int questionTypeId = Integer.parseInt(request.queryParams("questionTypeId"));
        if(questionTypeId < 3) {
            possibleAnswers = request.queryParams("possibleAnswers");
            if (null == possibleAnswers || possibleAnswers.isEmpty()) {
                sb.append("Mogući odgovori - obavezan podatak za odabrani tip pitanja<br>");
                result.setValid(false);
            }
        }
        if(questionTypeId < 4) {
            correctAnswers = request.queryParams("correctAnswers");
            if (null == correctAnswers || correctAnswers.isEmpty()) {
                sb.append("Točni odgovor/i - obavezan podatak - obavezan podatak za odabrani tip pitanja<br>");
                result.setValid(false);
            }
        }
        try {
            points = Integer.parseInt(request.queryParams("points"));
        } catch (NumberFormatException e){
            sb.append("Bodovi - mora biti brojčana vrijednost");
            result.setValid(false);
            throw e;
        }
        if(result.isValid()){
            result.setQuestion(new Question(subjectid, question, possibleAnswers, correctAnswers, points, questionTypeId));
            return result;
        } else {
            result.setQuestion(new Question(subjectid, question, possibleAnswers, correctAnswers, points, questionTypeId));
            result.setValidationMessage(sb.toString());
            return result;
        }
    }

    @Data
    private static class ValidationResult {
        Question question;
        boolean valid = true;
        String validationMessage;
    }
}
