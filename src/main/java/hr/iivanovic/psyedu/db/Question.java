package hr.iivanovic.psyedu.db;

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
    private String titleId;
    private String question;
    private String answers;
    private int points;

    public Question() {
    }

    public Question(int subjectId, String titleId, String question, String answers, int points){
        this.subjectId = subjectId;
        this.titleId = titleId;
        this.question = question;
        this.answers = answers;
        this.points = points;
    }


}
