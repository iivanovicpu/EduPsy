package hr.iivanovic.psyedu.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author iivanovic
 * @date 24.05.17.
 */
@Data
@AllArgsConstructor
public class ExternalLink {
    private int id;
    private int subjectId;
    private String title;
    private String url;
    private int linkTypeId;
}
