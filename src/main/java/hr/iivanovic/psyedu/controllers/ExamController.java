package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.controllers.QuestionType.ENTER_DESCRIPTIVE_ANSWER;
import static hr.iivanovic.psyedu.controllers.QuestionType.ENTER_SHORT_ANSWER;
import static hr.iivanovic.psyedu.db.Question.hasDescriptiveAnswer;
import static hr.iivanovic.psyedu.db.Question.hasShortAnswer;
import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hr.iivanovic.psyedu.db.AdaptiveRule;
import hr.iivanovic.psyedu.db.IntelligenceType;
import hr.iivanovic.psyedu.db.Question;
import hr.iivanovic.psyedu.db.TitleLearningStatus;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 09.10.16.
 */
public class ExamController extends AbstractController {
    private final static String EXAM = "/velocity/exam.vm";

    private static final double SUCCESSFUL_EXAM_PERCENT = 0.8;

    public static Route fetchQuestionsForTitle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        int subjectid = Integer.parseInt(request.params("subjectid"));
        User student = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            List<Question> questions = dbProvider.getAllQuestionsForSubject(subjectid, student.groupQuestions());
            Map<String, Object> model = createExamModel(request, subjectid, student, questions);
            model.put("validation", false);
            dbProvider.logLearningStatus(student.getId(), subjectid, TitleLearningStatus.OPENED_EXAM.getId());
            return ViewUtil.render(request, model, EXAM);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static String createHtmlQuestions(Request request, User student, List<Question> questions) {
        String htmlQuestions = "ispitna pitanja za ovo gradivo nisu unesena !";
        boolean writeSummary = student.getUserRules().stream().anyMatch(AdaptiveRule.P10_ASK_FOR_SUMMARY::equals);
        boolean askDescriptive = student.getUserRules().stream().anyMatch(AdaptiveRule.P9_QUESTIONS_HOW_WHAT_WHY::equals);
        boolean askShort = student.getUserRules().stream().anyMatch(AdaptiveRule.P11_SHORT_QUESTIONS::equals);
        List<Question> filteredQuestions = new LinkedList<>();
        if (askDescriptive) {
            filteredQuestions.addAll(questions.stream().filter(hasDescriptiveAnswer()).collect(Collectors.toList()));
            if (filteredQuestions.size() > 0 && !askShort) {
                return renderQuestions(filteredQuestions, LoginController.isStudent(request), writeSummary);
            }
        }
        if (askShort) {
            filteredQuestions.addAll(questions.stream().filter(hasShortAnswer()).collect(Collectors.toList()));
            if (filteredQuestions.size() > 0) {
                return renderQuestions(filteredQuestions, LoginController.isStudent(request), writeSummary);
            }
        }
        if (questions.size() > 0) {
            htmlQuestions = renderQuestions(questions, LoginController.isStudent(request), writeSummary);
        }
        return htmlQuestions;
    }

    public static Route submitExamQuestions = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        int subjectid = Integer.parseInt(request.queryParams("subjectid"));
        User student = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            Map<String, String> questionsWithAnswers = new HashMap<>();
            for (String param : request.queryParams()) {
                questionsWithAnswers.put(param, request.queryParams(param));
            }
            List<Question> questions = dbProvider.getAllQuestionsForSubject(subjectid, student.groupQuestions());
            StringBuilder sb = new StringBuilder();

            // ako student ima aktivno pravilo P14 (immutable), validira se test s općom inteligencijom (ona se ne update-a)
            IntelligenceType intelligenceType = student.hasImmutableIntelligenceTypeRule() ? IntelligenceType.O : student.getIntelligenceType();
            boolean success = validateExam(questions, questionsWithAnswers, sb, subjectid, student, intelligenceType);
            student.resolveIntelligenceType();
            Map<String, Object> model = createExamModel(request, subjectid, student, questions);
            model.put("validation", sb);
            model.put("success", success);

            if (success) {
                dbProvider.logLearningStatus(student.getId(), subjectid, TitleLearningStatus.FINISHED_EXAM.getId());
            }
            return ViewUtil.render(request, model, EXAM);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static Map<String, Object> createExamModel(Request request, int subjectid, User student, List<Question> questions) {
        Map<String, Object> model = new HashMap<>();
        String htmlQuestions = createHtmlQuestions(request, student, questions);
        model.put("questions", htmlQuestions);
        model.put("subjectId", subjectid);
        return model;
    }

    private static boolean validateExam(List<Question> questions, Map<String, String> questionsWithAnswers, StringBuilder sb, int subjectId, User student, IntelligenceType intelligenceType) {
        // ne računaj bodove za esejska pitanja todo: provjeriti
        boolean shortQuestions = student.shortQuestions();
        double sumOfPoints = questions.stream()
                .filter(hasDescriptiveAnswer())
                .filter(question -> question.getQuestionTypeId() == QuestionType.ENTER_SHORT_ANSWER.getId() && shortQuestions)
                .filter(question -> question.getQuestionTypeId() <= 2 && !shortQuestions)
                .mapToInt(Question::getPoints).sum();
        // todo: ovdje isfiltrirati i pitanja koja nisu prikazana po pravilima
        // todo: ako je max broj mogućih bodova 0 - success
        System.out.println("ukupni broj bodova: " + sumOfPoints);
        ValueHolder<Double> successPoints = new ValueHolder<>(0d);
        questionsWithAnswers.forEach((key, answer) -> {
            String questionHtmlId = key.split("_")[0];
            if (!questionHtmlId.toLowerCase().contains("subjectid".toLowerCase()) && !questionHtmlId.toLowerCase().contains("summary".toLowerCase())) {
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
                if (dbQuestion.getQuestionTypeId() == ENTER_DESCRIPTIVE_ANSWER.getId()) {
                    // todo: provjeriti što s ovime
                }
                if (dbQuestion.getQuestionTypeId() == ENTER_SHORT_ANSWER.getId()) {
                    match = dbQuestion.getCorrectAnswers().toLowerCase().trim().contains(answer.toLowerCase().trim());
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
        saveStudentSuccess(subjectId, student.getId(), result, success, intelligenceType);
        return success;
    }

    private static void saveStudentSuccess(int subjectId, int studentId, double result, boolean success, IntelligenceType intelligenceType) {
        if (dbProvider.studentScoreExists(subjectId, studentId)) {
            // todo: provjeriti da li prepisati stari rezultat (iako je možda uspješniji ??)
            dbProvider.updateStudentScore(subjectId, studentId, result, success);
        } else {
            dbProvider.createStudentScore(subjectId, studentId, result, success);
        }
        if (!success) {
            // todo: update intelligenceType
            dbProvider.decreaseIntelligenceTypePoints(studentId, intelligenceType);
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
