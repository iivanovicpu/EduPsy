package hr.iivanovic.psyedu.util;

import lombok.Getter;

public class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static class Web {
        @Getter public static final String INDEX = "/index/";
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String BOOKS = "/books/";
        @Getter public static final String ONE_BOOK = "/books/:isbn/";

        @Getter public static final String SUBJECTS = "/subjects/";
        @Getter public static final String ONE_SUBJECTS = "/subjects/:id/";
        @Getter public static final String EDIT_SUBJECT = "/subject/";
    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public final static String BOOKS_ALL = "/velocity/book/all.vm";
        public static final String BOOKS_ONE = "/velocity/book/one.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";

        public final static String SUBJECTS_ALL = "/velocity/subjects/all.vm";
        public final static String SUBJECTS_ONE = "/velocity/subjects/one.vm";
        public final static String SUBJECTS_ONE_EDIT = "/velocity/subjects/edit.vm";
    }

}
