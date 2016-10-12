package hr.iivanovic.psyedu.controllers;

import java.util.Arrays;

/**
 * @author iivanovic
 * @date 12.10.16.
 */
public enum AdaptiveRuleTypes {
    SHOW(1, "Prikaži dodatno"),
    HIDE(2, "Ne prikazuj"),
    SPLIT(3, "Odlomci do 400 znakova");

    private final int id;

    private final String description;

    AdaptiveRuleTypes(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static AdaptiveRuleTypes getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == (id)).findFirst().orElse(null);
    }
}
