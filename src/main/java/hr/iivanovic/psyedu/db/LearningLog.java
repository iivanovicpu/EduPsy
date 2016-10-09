package hr.iivanovic.psyedu.db;

import java.util.Date;

import lombok.Data;

/**
 * @author iivanovic
 * @date 08.10.16.
 */
@Data
public class LearningLog {
    private int id;
    private int studentId;
    private int subjectId;
    private String titleId;
    private Date date;
    private int statusId;
}
