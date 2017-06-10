package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.controllers.QuestionType.ENTER_DESCRIPTIVE_ANSWER;
import static hr.iivanovic.psyedu.controllers.QuestionType.ENTER_SHORT_ANSWER;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.iivanovic.psyedu.db.AdaptiveRule;
import hr.iivanovic.psyedu.db.Question;
import hr.iivanovic.psyedu.db.TitleLearningStatus;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 09.10.16.
 */
public class ExamController extends AbstractController {

    private static final double SUCCESSFUL_EXAM_PERCENT = 0.8;

    public static Route fetchQuestionsForTitle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        int subjectid = Integer.parseInt(request.params("subjectid"));
        User student = LoginController.getCurrentUser(request);
        boolean writeSummary = student.getUserRules().stream().anyMatch(AdaptiveRule.P10_ASK_FOR_SUMMARY::equals);

        if (clientAcceptsHtml(request)) {
            Map<String, Object> model = new HashMap<>();
            List<Question> questions = dbProvider.getAllQuestionsForSubjectAndTitle(subjectid);
            String htmlQuestions = "ispitna pitanja za ovo gradivo nisu unesena !";
            if (questions.size() > 0) {
                htmlQuestions = renderQuestions(questions, LoginController.isStudent(request), writeSummary);
            }
            model.put("questions", htmlQuestions);
            model.put("subjectId", subjectid);
            model.put("validation", false);
            dbProvider.logLearningStatus(student.getId(), subjectid, TitleLearningStatus.OPENED_EXAM.getId());
            return ViewUtil.render(request, model, Path.Template.EXAM);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitExamQuestions = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        int subjectid = Integer.parseInt(request.queryParams("subjectid"));
        User student = LoginController.getCurrentUser(request);
        boolean writeSummary = student.getUserRules().stream().anyMatch(AdaptiveRule.P10_ASK_FOR_SUMMARY::equals);
        if (clientAcceptsHtml(request)) {
            Map<String, String> questionsWithAnswers = new HashMap<>();
            for (String param : request.queryParams()) {
                questionsWithAnswers.put(param, request.queryParams(param));
                System.out.println(param + " " + request.queryParams(param));
            }
            List<Question> questions = dbProvider.getAllQuestionsForSubjectAndTitle(subjectid);
            StringBuilder sb = new StringBuilder();

            boolean success = validateExam(questions, questionsWithAnswers, sb, subjectid, student.getId());
            String htmlQuestions = "ispitna pitanja za ovo gradivo nisu unesena !";

            Map<String, Object> model = new HashMap<>();
            if (questions.size() > 0) {
                htmlQuestions = renderQuestions(questions, LoginController.isStudent(request), writeSummary);
            }
            model.put("questions", htmlQuestions);
            model.put("subjectId", subjectid);

            model.put("validation", sb);
            model.put("success", success);

            if (success) {
                dbProvider.logLearningStatus(student.getId(), subjectid, TitleLearningStatus.FINISHED_EXAM.getId());
            }
            return ViewUtil.render(request, model, Path.Template.EXAM);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static boolean validateExam(List<Question> questions, Map<String, String> questionsWithAnswers, StringBuilder sb, int subjectId, int studentId) {
        // ne računaj bodove za esejska pitanja todo: provjeriti
        double sumOfPoints = questions.stream().filter(question -> question.getQuestionTypeId() != ENTER_DESCRIPTIVE_ANSWER.getId()).mapToInt(Question::getPoints).sum();
        ValueHolder<Double> successPoints = new ValueHolder<>(0d);
        questionsWithAnswers.forEach((key, answer) -> {
            String questionHtmlId = key.split("_")[0];
            if (!questionHtmlId.toLowerCase().contains("subjectid".toLowerCase())) {
                int questionId = Integer.parseInt(questionHtmlId);
                Question dbQuestion = questions.stream().filter(question -> question.getId() == questionId).findFirst().get();
                boolean match;
                if (dbQuestion.getQuestionTypeId() < 3) {
                    String[] correctAnswers = dbQuestion.getCorrectAnswers().split(",");
                    double pointsByAnswer = dbQuestion.getPoints() / correctAnswers.length;
                    for (String correctAnswer : correctAnswers) {
                        match = correctAnswer.trim().toLowerCase().contains(answer.toLowerCase().trim());
                        System.out.println(correctAnswer + " - " + answer + " - " + match);
                        if (match) {
                            successPoints.setValue(pointsByAnswer + successPoints.getValue());
                        }
                    }
                }
                if(dbQuestion.getQuestionTypeId() == ENTER_DESCRIPTIVE_ANSWER.getId()){
                    // todo: provjeriti što s ovime
                }
                if (dbQuestion.getQuestionTypeId() == ENTER_SHORT_ANSWER.getId() ){
                    match =  dbQuestion.getCorrectAnswers().toLowerCase().trim().contains(answer.toLowerCase().trim());
                    System.out.println(answer + " " + match);
                    if (match) {
                        successPoints.setValue(dbQuestion.getPoints() + successPoints.getValue());
                    }
                }
            }
        });
        double result = successPoints.getValue() / sumOfPoints;
        boolean success = SUCCESSFUL_EXAM_PERCENT <= result;
        if (!success) {
            sb.append("Nažalost, ispit nije uspješno riješen. Postotak: ").append(result * 100).append("%");
            sb.append("\n(").append(successPoints.getValue()).append("/").append(sumOfPoints).append(")");
        } else {
            sb.append("Bravo, ispit je uspješno riješen. Postotak: ").append(result * 100).append("%");
            sb.append("\n(").append(successPoints.getValue()).append("/").append(sumOfPoints).append(")");
        }
        saveStudentSuccess(subjectId, studentId, result, success);
        return success;
    }

    private static void saveStudentSuccess(int subjectId, int studentId, double result, boolean success) {
        if (dbProvider.studentScoreExists(subjectId, studentId)) {
            // todo: provjeriti da li prepisati stari rezultat (iako je možda uspješniji ??)
            dbProvider.updateStudentScore(subjectId, studentId, result, success);
        } else {
            dbProvider.createStudentScore(subjectId, studentId, result, success);
        }
    }

    private static class ValueHolder<T> {
        private T value;

        ValueHolder(T value) {
            this.value = value;
        }

        public T getValue() {
            return this.value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
