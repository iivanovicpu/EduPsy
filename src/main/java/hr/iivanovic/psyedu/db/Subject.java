package hr.iivanovic.psyedu.db;

import lombok.Data;

/**
 * @author iivanovic
 * @date 28.08.16.
 */
@Data
public class Subject {
    private int id;
    private String title;
    private String keywords;
    private String url;
    private int subjectId;
    private int subjectLevelId;
    private int order;
    private String content;
}
