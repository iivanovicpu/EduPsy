package hr.iivanovic.psyedu.db;

import java.util.Arrays;

/**
 * @author iivanovic
 * @date 08.10.16.
 */
public enum TitleLearningStatus {
    NONE(0),
    OPENED(1),
    LEARNED(2),
    OPENED_EXAM(3),
    FINISHED_EXAM(4);

    private final int id;

    TitleLearningStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TitleLearningStatus getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == (id)).findFirst().orElse(null);
    }

    public boolean isFinished(){
        return id >= FINISHED_EXAM.getId();
    }
}
