package hr.iivanovic.psyedu.db;

import java.util.UUID;

import lombok.Data;

/**
 * @author iivanovic
 * @date 28.08.16.
 */
@Data
public class User {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
