package hr.iivanovic.psyedu.db;

import lombok.Data;

/**
 * @author iivanovic
 * @date 28.08.16.
 */
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String color;
    private int intelligenceTypeId;
    private int learningStyleId;
    private boolean completedLearningStylePoll;
    private boolean completedIntelligencePoll;

    private int lsPointsActive;
    private int lsPointsReflective;
    private int lsPointsVisual;
    private int lsPointsVerbal;
    private int lsPointsSequential;
    private int lsPointsGlobal;
    private int lsPointsIntuitive;
    private int lsPointsSensor;

}
