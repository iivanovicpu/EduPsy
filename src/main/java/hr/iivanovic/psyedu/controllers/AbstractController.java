package hr.iivanovic.psyedu.controllers;

import java.util.List;

import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Question;
import hr.iivanovic.psyedu.db.Sql2oModel;

/**
 * @author iivanovic
 * @date 31.08.16.
 */
public class AbstractController {
    public static Model dbProvider = Sql2oModel.getInstance();

    protected static String renderQuestions(List<Question> questions, boolean isStudent) {
        StringBuilder sb = new StringBuilder();
        for (Question question : questions) {
            sb.append("<div class=\"form-group\">");
            sb.append("<hr>").append(question.getQuestion());
            QuestionType questionType = QuestionType.getById(question.getQuestionTypeId());

            if (QuestionType.SELECT_MULTIPLE_ANSWERS.equals(questionType)) {
                renderCheckBoxesAnswer(sb, question);
            }
            if (QuestionType.SELECT_ONE_ANSWER.equals(questionType)) {
                renderRadioButtonsAnswer(sb, question);
            }
            if (QuestionType.ENTER_SHORT_ANSWER.equals(questionType)) {
                renderTextInputAnswer(sb, question);
            }
            if (QuestionType.ENTER_DESCRIPTIVE_ANSWER.equals(questionType)) {
                renderTextAreaAnswer(sb, question);
            }
            if(!isStudent) {
                sb.append("<form method=\"post\" action=\"/deletequestion/\">");
                sb.append("<input type=\"hidden\" name=\"questionid\" value=\"").append(question.getId()).append("\">");
                sb.append("<input type=\"hidden\" name=\"subjectid\" value=\"").append(question.getSubjectId()).append("\">");
                sb.append("<button type=\"submit\" class=\"btn btn-danger\">Briši</button>\n");
                sb.append("Riješenje: ").append(question.getCorrectAnswers());
                sb.append("</form>");
            }
            sb.append("</div>");
        }
        return sb.toString();
    }

    private static void renderTextAreaAnswer(StringBuilder sb, Question question) {
        sb.append("<br><textarea name=\"").append(question.getId()).append("_").append(question.getId()).append("\" rows=\"10\" cols=\"30\"></textarea>");
    }

    private static void renderTextInputAnswer(StringBuilder sb, Question question) {
        sb.append("<br><input type=\"text\" name=\"").append(question.getId()).append("_").append(question.getId()).append("\">");
    }

    private static void renderRadioButtonsAnswer(StringBuilder sb, Question question) {
        String questionPossibleAnswers = question.getPossibleAnswers();
        String[] possibleAnswers = null != questionPossibleAnswers ? questionPossibleAnswers.split(",") : new String[0];
        for (String answer : possibleAnswers) {
            sb.append("<div class=\"radio\">");
            sb.append("<label><input type=\"radio\" name=\"").append(question.getId()).append("_").append(question.getId())
                    .append("\" value=\"").append(answer).append("\">")
                    .append(answer).append("</label>");
            sb.append("</div>");
        }
    }

    private static void renderCheckBoxesAnswer(StringBuilder sb, Question question) {
        int idx = 0;
        String questionPossibleAnswers = question.getPossibleAnswers();
        String[] possibleAnswers = null != questionPossibleAnswers ? questionPossibleAnswers.split(",") : new String[0];
        for (String answer : possibleAnswers) {
            sb.append("<div class=\"checkbox\">");
            String questionId = question.getId() + "_" + idx++;
            sb.append("<label><input type=\"checkbox\" name=\"").append(questionId).append("\" value=\"").append(answer).append("\">").append(answer).append("</label>");
            sb.append("</div>");
        }
    }
}
