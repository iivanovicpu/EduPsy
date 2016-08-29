package hr.iivanovic.psyedu.db;

import java.util.UUID;

import lombok.Data;

/**
 * @author iivanovic
 * @date 28.08.16.
 */
@Data
public class Subject {
    private long id;
    private String title;
    private String keywords;
    private String url;
}
