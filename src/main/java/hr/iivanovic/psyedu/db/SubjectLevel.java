package hr.iivanovic.psyedu.db;

/**
 * @author iivanovic
 * @date 12.04.17.
 */
public enum SubjectLevel {
    OSNOVNO(1),
    NAPREDNO(2);

    private final int id;

    SubjectLevel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
