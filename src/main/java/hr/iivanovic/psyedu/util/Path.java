package hr.iivanovic.psyedu.util;

import lombok.Getter;

public class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static class Web {
        @Getter public static final String LOGIN = "/login/";

        @Getter public static final String SUBJECTS = "/subjects/";
        @Getter public static final String EDIT_SUBJECT = "/edit_subject/:id/";
        @Getter public static final String INTELLIGENCE_POLL = "/intelligencepoll/";
        @Getter public static final String LEARNING_STYLE_POLL = "/learningstylepoll/";
    }
}
