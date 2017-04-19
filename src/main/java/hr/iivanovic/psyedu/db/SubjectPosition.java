package hr.iivanovic.psyedu.db;

import java.util.Arrays;

/**
 * @author iivanovic
 * @date 19.04.17.
 */
public enum SubjectPosition {
    PREDMET(1,"Predmet", 2),
    CJELINA(2,"Cjelina", 3),
    JEDINICA(3,"Jedinica", 4),
    CESTICA(4,"Čestica", null);

    private final int id;

    private final String description;

    private final Integer subPosition;

    SubjectPosition(int id, String description, Integer subPosition) {
        this.id = id;
        this.description = description;
        this.subPosition = subPosition;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public SubjectPosition getSubPosition() {
        return null != subPosition ? getById(subPosition) : null;
    }

    public static SubjectPosition getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == (id)).findFirst().orElse(null);
    }
}
