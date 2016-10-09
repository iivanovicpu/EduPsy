package hr.iivanovic.psyedu.util;

import hr.iivanovic.psyedu.db.Sql2oModel;

/**
 * @author iivanovic
 * @date 14.09.16.
 */
public class InitDb {
    Sql2oModel sql2oModel = Sql2oModel.getInstance();

    public void init() {
        clearRecords();
        createSubjects();
        createUsers();
    }

    private void clearRecords() {
        sql2oModel.clearRecordsForReinit();
    }

    public void createUsers() {
        sql2oModel.createUser("iivanovic", "password", "Igor", "Ivanović", "iivanovic.pu@gmail.com", "ADMIN");
        sql2oModel.createUser("jzufic", "password", "Janko", "Žufić", "janko.zufic@gmail.com", "TEACHER");
        sql2oModel.createUser("mmarkovic", "password", "Marko", "Marković", "mmarkovic@mail.net", "STUDENT");
        sql2oModel.createUser("iivic", "password", "Ivo", "Ivić", "ivo.ivic@dmail.net.com", "STUDENT");
        sql2oModel.createUser("vnovak", "password", "Vjenceslav", "Novak", "vnovak@air.net.com", "STUDENT");
    }

    public void createSubjects() {
        sql2oModel.createSubject("Edu Psy - html system", "edupsy,html,system", "/materijali/edupsy.html");
        sql2oModel.createSubject("Relacijske baze podataka", "baze podataka,ralacije,sql", "/materijali/relacijskebazepodataka.html");
        sql2oModel.createSubject("Matematika", "matematika, linearna algebra, funkcije, jednadžbe, limes", "/materijali/matematika.html");
    }
}

