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
        @Getter public static final String VIEW_SUBJECT = "/subjects/:id/";
        @Getter public static final String EDIT_SUBJECT = "/edit_subject/:id/";
        @Getter public static final String EDIT_SUBJECT_ITEM = "/edit_subject_item/:id/:parentId/:action/";
        @Getter public static final String ADD_SUBJECT_ITEM = "/add_subject_item/:subjectId/";
        @Getter public static final String SUBMIT_EDITED_SUBJECT = "/edit_subject/";
        @Getter public static final String ADD_SUBJECT = "/addsubject/";
        @Getter public static final String ONE_PARENT_TITLE = "/onetitle/:id/";
        @Getter public static final String ONE_CHILD_TITLE = "/onetitlechild/:id/";
        @Getter public static final String ONE_SUBJECT_QUESTIONS = "/admin_subject/:id/";
        @Getter public static final String ONE_TITLE_QUESTIONS = "/onetitlequestions/:subjectid/";
        @Getter public static final String PERSONAL_PROFILE = "/profile/";
        @Getter public static final String EXAM = "/exam/:subjectid/:titleid/";
        @Getter public static final String STUDENTS = "/students/";
        @Getter public static final String ADDSTUDENT = "/addstudent/";
        @Getter public static final String ADMIN_RULES = "/adminrules/";
        @Getter public static final String INTELLIGENCE_POLL = "/intelligencepoll/";
        @Getter public static final String LEARNING_STYLE_POLL = "/learningstylepoll/";
        @Getter public static final String STUDENT_DETAILS = "/studentdtl/:id/";
    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";

        public final static String SUBJECTS_ALL = "/velocity/subjects/all.vm";
        public final static String SUBJECTS_ONE = "/velocity/subjects/one.vm";
        public final static String EDIT_SUBJECT = "/velocity/subjects/edit_subject.vm";
        public final static String EDIT_SUBJECT_ITEM = "/velocity/subjects/edit_subject_item.vm";
        public final static String SUBJECTS_ONE_QUESTIONS = "/velocity/subjects/onesubjectquestions.vm";
        public final static String SUBJECTS_ONE_EDIT = "/velocity/subjects/edit.vm";
        public final static String SUBJECT_ADD = "/velocity/subjects/add.vm";
        public final static String SUBJECT_ONE_TITLE = "/velocity/subjects/title.vm";
        public final static String SUBJECT_ONE_TITLE_QUESTIONS = "/velocity/subjects/onetitlequestions.vm";
        public final static String PERSONAL_PROFILE = "/velocity/profile.vm";
        public final static String EXAM = "/velocity/exam.vm";
        public final static String STUDENTS = "/velocity/students.vm";
        public final static String STUDENTS_ADD = "/velocity/addstudent.vm";
        public final static String STUDENT_DETAILS = "/velocity/studentDetails.vm";
        public final static String ADAPTIVE_RULES_ADMIN = "/velocity/adaptiverules.vm";
        public final static String POLL_INTELLIGENCE_TYPE = "/velocity/pollIntelligenceType.vm";
        public final static String POLL_LEARNING_STYLE = "/velocity/pollLearningType.vm";
    }

}
