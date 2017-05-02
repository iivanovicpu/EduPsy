package hr.iivanovic.psyedu.db;

import java.util.Arrays;

/**
 * @author iivanovic
 * @date 12.10.16.
 */
public enum IntelligenceType {
    O(1, "O - Opća"),
    ML(2, "ML - Logičko-matematička"),
    NV(3, "NV - Prostorna"),
    V(4, "V -Lingvistička");

    private final int id;

    private final String description;

    IntelligenceType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static IntelligenceType getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == (id)).findFirst().orElse(null);
    }
}
