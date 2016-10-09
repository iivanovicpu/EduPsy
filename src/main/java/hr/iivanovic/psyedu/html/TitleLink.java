package hr.iivanovic.psyedu.html;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TitleLink {
    private String title;
    private String id;
    private long subjectId;
    private String classAttribute;
    private int learningLogStatus;
}