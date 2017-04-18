package hr.iivanovic.psyedu.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author iivanovic
 * @date 28.08.16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    private int id;
    private String title;
    private String keywords;
    private String url;
    private Integer subjectId;
    private Integer parentSubjectId;
    private int subjectLevelId;
    private int order;
    private String content;

    private String additionalContent;
}
