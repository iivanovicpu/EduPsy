package hr.iivanovic.psyedu.db;

import java.util.Arrays;

/**
 * @author iivanovic
 * @date 09.04.17.
 */
public enum AdaptiveRule {
    SHOW_ADVANCED_SUBJECTS(1,"Prikaži materijale za napredne", "prikazuju se materijali s oznakom 'napredno'"),
    SHOW_LINKS(2,"Prikaži vanjske poveznice", "prikazuje poveznice na vanjske izvore vezane za materijale za učenje"),
    TEXT_MAX_1000(3,"Tekst do 1000 znakova", "tekst se prelama na paragrafe do 1000 znakova s odlomcima po 150 do 400 znakova"),
    BIGGER_PICTURES(4,"Prikaži veće slike", "slike u materijalima prikazuju se veće"),
    SEQUENTIAL_NAVIGATION(5,"Sekvencijalna navigacija", "otvaranje nove cjeline moguće je tek nakon što je obrađena tekuća cjelina (nastavna pitanja riješena minimalno 80%)"),
    KEYWORDS_HIGHLIGHTING(6,"Istakni ključne riječi", "dodatno isticanje ključnih riječi"),
    QUESTIONS_GROUPING(7,"Pitanja po cjelini", "prikaz ispitnih pitanja po obrađenoj cjelini (za sve čestice cjeline)"),
    LONGER_TEXT(8,"Prikaz duljeg teksta", "prikaz teksta i do 3000 znakova s odlomcima po 150 do 400 znakova"),
    QUESTIONS_HOW_WHAT_WHY(9,"Pitanja \"Kako\", \"Što\", \"Zašto\"", "više pitanja koja s odgovorima u obliku eseja"),
    ASK_ESSAY(10,"Zahtjevaj sažetak o naučenom", "zahtjeva se pisanje sažetka u obliku eseja o naučenom (sustav provjerava broj ključnih riječi koje esej sadrži)"),
    SHORT_QUESTIONS(11,"Kratka pitanja", "više pitanja s kratkim odgovorima, da/ne odgovorima i pitanja s jednostrukim ili višestrukim izborom"),
    SEQUENTIAL_ANSWER_CHECK(12,"Trenutna provjera odgovora", "provjera odgovora na pitanje odmah nakon pojedinačnog upisa odgovora"),
            ;

    private final int id;
    private final String title;
    private final String description;

    AdaptiveRule(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static AdaptiveRule getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == (id)).findFirst().orElse(null);
    }
}
