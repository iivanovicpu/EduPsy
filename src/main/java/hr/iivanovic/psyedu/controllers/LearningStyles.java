package hr.iivanovic.psyedu.controllers;

import java.util.Arrays;

/**
 * @author iivanovic
 * @date 12.10.16.
 */
public enum LearningStyles {
    GENERAL(0, "O - Opći"),
    STYLE_1_AKT(1,"AKT - Aktivni"),
    STYLE_1_REF(2,"REF - Reflektivni"),
    STYLE_1_NAR(3,"NAR - Neutralni"),
    STYLE_2_AKT(4,"VIZ - Vizualni"),
    STYLE_2_REF(5,"VER - Verbalni"),
    STYLE_2_NAR(6,"NVV - Neutralni"),
    STYLE_3_AKT(7,"SEK - Sekvencijalni"),
    STYLE_3_REF(8,"GLO - Globalni"),
    STYLE_3_NAR(9,"NSG - Neutralni"),
    STYLE_4_AKT(10,"INT - Intuitivni"),
    STYLE_4_REF(11,"SEN - Senzorni"),
    STYLE_4_NAR(12,"NIS - Neutralni");

    private final int id;

    private final String description;

    LearningStyles(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static LearningStyles getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == (id)).findFirst().orElse(null);
    }
}
