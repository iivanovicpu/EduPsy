package hr.iivanovic.psyedu.controllers;

/**
 * @author iivanovic
 * @date 03.10.16.
 */
public enum QuestionType {
    SELECT(1),
    ENTER_ANSWER(2),
    ENTER_DESCRIPTIVE(3);

    private final int id;

    QuestionType(int id) {
        this.id = id;
    }
}
