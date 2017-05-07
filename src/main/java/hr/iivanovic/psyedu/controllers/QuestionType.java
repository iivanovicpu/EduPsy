package hr.iivanovic.psyedu.controllers;

import java.util.Arrays;

/**
 * @author iivanovic
 * @date 03.10.16.
 */
public enum QuestionType {
    SELECT_MULTIPLE_ANSWERS(1,"odabir više točnih odgovora"),
    SELECT_ONE_ANSWER(2, "odabir jednog točnog odgovora"),
    ENTER_SHORT_ANSWER(3, "kratki odgovor"),
    ENTER_DESCRIPTIVE_ANSWER(4, "odgovor u obliku eseja");

    private final int id;

    private final String name;

    QuestionType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static QuestionType getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == (id)).findFirst().orElse(null);
    }
}
