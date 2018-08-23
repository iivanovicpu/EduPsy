package hr.iivanovic.psyedu.controllers;

import java.util.Arrays;

/**
 * @author iivanovic
 * @date 12.10.16.
 */
public enum AdaptiveRuleTypes {
    SHOW(1, "Prikaži Dodatno","**SHOW**"),
    HIDE(2, "Ne prikazuj","**HIDE**"),
    SPLIT(3, "Odlomci do 400 znakova","**400**");

    private final int id;

    private final String description;

    private final String mark;

    AdaptiveRuleTypes(int id, String description, String mark) {
        this.id = id;
        this.description = description;
        this.mark = mark;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getMark() {
        return mark;
    }

    public static AdaptiveRuleTypes getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == (id)).findFirst().orElse(null);
    }
}
