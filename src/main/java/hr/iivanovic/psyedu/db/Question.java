package hr.iivanovic.psyedu.db;

import java.util.function.Predicate;

import hr.iivanovic.psyedu.controllers.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author iivanovic
 * @date 01.10.16.
 */
@Data
@AllArgsConstructor
public class Question {
    private int id;
    private int subjectId;
    private int questionTypeId;
    private String question;
    private String possibleAnswers;
    private String correctAnswers;
    private int points;

    public Question(int subjectId, String question, String possibleAnswers, String correctAnswers, int points, int questionTypeId){
        this.subjectId = subjectId;
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswers = correctAnswers;
        this.points = points;
        this.questionTypeId = questionTypeId;
    }

    public static Predicate<Question> hasDescriptiveAnswer(){
        return question -> question.getQuestionTypeId() == QuestionType.ENTER_DESCRIPTIVE_ANSWER.getId();
    }

    public static Predicate<Question> hasShortAnswer(){
        return question -> question.getQuestionTypeId() == QuestionType.ENTER_SHORT_ANSWER.getId();
    }


}
