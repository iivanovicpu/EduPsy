package hr.iivanovic.psyedu.controllers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sql2o.Sql2o;

import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Question;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.util.DbUtil;

/**
 * @author iivanovic
 * @date 31.08.16.
 */
public class AbstractController {
//    public static Sql2o sql2o = DbUtil.getH2DataSource();
    public static Model dbProvider = Sql2oModel.getInstance();

    protected static QuestionType getQuestionType(String answers) {
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
            return QuestionType.SELECT;
        if (countCorrect == 1 && countSelections == 0)
            return QuestionType.ENTER_ANSWER;
        return QuestionType.ENTER_DESCRIPTIVE;
    }

    protected static String renderQuestions(List<Question> questions) {
        StringBuilder sb = new StringBuilder();
        for (Question question : questions) {
            sb.append("<hr>").append(question.getQuestion());
            QuestionType questionType = getQuestionType(question.getAnswers());
            String[] answers = question.getAnswers().split("\\|");
            for (int i = 0; i < answers.length; i++) {
                String answer = answers[i].replaceAll("\\*","");
                if(QuestionType.SELECT.equals(questionType)) {
                    sb.append("<br><input type=\"checkbox\" name=\"").append(question.getId()).append("_").append(i).append("\" value=\"").append(answer).append("\">").append(answer);
                }
                if(QuestionType.ENTER_ANSWER.equals(questionType)){
                    sb.append("<br><input type=\"text\" name=\"").append(question.getId()).append("_").append(i).append("\">");
                }
                if(QuestionType.ENTER_DESCRIPTIVE.equals(questionType)){
                    sb.append("<br><textarea name=\"").append(question.getId()).append("_").append(i).append("\" rows=\"10\" cols=\"30\"></textarea>");
                }
            }
        }
        return sb.toString();
    }
}
