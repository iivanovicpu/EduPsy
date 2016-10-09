package hr.iivanovic.psyedu.db;

/**
 * @author iivanovic
 * @date 08.10.16.
 */
public enum TitleLearningStatus {
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
}
