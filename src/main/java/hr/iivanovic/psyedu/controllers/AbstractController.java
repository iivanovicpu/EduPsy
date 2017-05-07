package hr.iivanovic.psyedu.controllers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Question;
import hr.iivanovic.psyedu.db.Sql2oModel;

/**
 * @author iivanovic
 * @date 31.08.16.
 */
public class AbstractController {
    public static Model dbProvider = Sql2oModel.getInstance();

    /*protected static QuestionType getQuestionType(String answers) {
        Pattern patternCorrect = Pattern.compile("\\*");
        Pattern patternSelections = Pattern.compile("\\|");
        Matcher matcher = patternCorrect.matcher(answers);
        int countCorrect = 0;
        while (matcher.find())
            countCorrect++;

        int countSelections = 0;
        matcher = patternSelections.matcher(answers);
        while (matcher.find())
            countSelections++;

        if (countCorrect >= 1 && countSelections >= 1)
            return QuestionType.SELECT_MULTIPLE_ANSWERS;
        if (countCorrect == 1 && countSelections == 0)
            return QuestionType.ENTER_SHORT_ANSWER;
        return QuestionType.ENTER_DESCRIPTIVE_ANSWER;
    }*/

    protected static String renderQuestions(List<Question> questions) {
        StringBuilder sb = new StringBuilder();
        for (Question question : questions) {
            sb.append("<hr>").append(question.getQuestion());
            QuestionType questionType = QuestionType.getById(question.getQuestionTypeId());


            if(QuestionType.SELECT_MULTIPLE_ANSWERS.equals(questionType)){
                String questionPossibleAnswers = question.getPossibleAnswers();
                String[] possibleAnswers = null != questionPossibleAnswers ? questionPossibleAnswers.split(",") : new String[0];
                for (int i = 0; i < possibleAnswers.length; i++) {
                    String answer = possibleAnswers[i];
                    sb.append("<br><input type=\"checkbox\" name=\"").append(question.getId()).append("_").append(i).append("\" value=\"").append(answer).append("\">").append(answer);
                }
            }
            if(QuestionType.SELECT_ONE_ANSWER.equals(questionType)){
                String questionPossibleAnswers = question.getPossibleAnswers();
                String[] possibleAnswers = null != questionPossibleAnswers ? questionPossibleAnswers.split(",") : new String[0];
                for (int i = 0; i < possibleAnswers.length; i++) {
                    String answer = possibleAnswers[i];
                    sb.append("<br><input type=\"radio\" name=\"").append(question.getId()).append("_").append(i).append("\" value=\"").append(answer).append("\">").append(answer);
                }
            }
            if(QuestionType.ENTER_SHORT_ANSWER.equals(questionType)){
                    sb.append("<br><input type=\"text\" name=\"").append(question.getId()).append("_").append(question.getId()).append("\">");
            }
                if(QuestionType.ENTER_DESCRIPTIVE_ANSWER.equals(questionType)){
                    sb.append("<br><textarea name=\"").append(question.getId()).append("_").append(question.getId()).append("\" rows=\"10\" cols=\"30\"></textarea>");
                }
            }
        return sb.toString();
    }
}
