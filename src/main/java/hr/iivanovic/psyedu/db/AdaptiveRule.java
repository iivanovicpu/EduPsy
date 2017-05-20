package hr.iivanovic.psyedu.db;

import java.util.Arrays;

/**
 * @author iivanovic
 * @date 09.04.17.
 */
public enum AdaptiveRule {
    P1_SHOW_ADVANCED_SUBJECTS(1,"Prikaži materijale za napredne", "prikazuju se materijali s oznakom 'napredno'"),
    P2_SHOW_LINKS(2,"Prikaži vanjske poveznice", "prikazuje poveznice na vanjske izvore vezane za materijale za učenje"),
    P3_TEXT_MAX_1000(3,"Tekst do 1000 znakova", "tekst se prelama na paragrafe do 1000 znakova s odlomcima po 150 do 400 znakova"),
    P4_BIGGER_PICTURES(4,"Prikaži veće slike", "slike u materijalima prikazuju se veće"),
    P5_SEQUENTIAL_NAVIGATION(5,"Sekvencijalna navigacija", "otvaranje nove cjeline moguće je tek nakon što je obrađena tekuća cjelina (nastavna pitanja riješena minimalno 80%)"),
    P6_KEYWORDS_HIGHLIGHTING(6,"Istakni ključne riječi", "dodatno isticanje ključnih riječi"),
    P7_QUESTIONS_GROUPING(7,"Pitanja po cjelini", "prikaz ispitnih pitanja po obrađenoj cjelini (za sve čestice cjeline)"),
    P8_LONGER_TEXT(8,"Prikaz duljeg teksta", "prikaz teksta i do 3000 znakova s odlomcima po 150 do 400 znakova"),
    P9_QUESTIONS_HOW_WHAT_WHY(9,"Pitanja \"Kako\", \"Što\", \"Zašto\"", "više pitanja koja s odgovorima u obliku eseja"),
    P10_ASK_ESSAY(10,"Zahtjevaj sažetak o naučenom", "zahtjeva se pisanje sažetka u obliku eseja o naučenom (sustav provjerava broj ključnih riječi koje esej sadrži)"),
    P11_SHORT_QUESTIONS(11,"Kratka pitanja", "više pitanja s kratkim odgovorima, da/ne odgovorima i pitanja s jednostrukim ili višestrukim izborom"),
    P12_SEQUENTIAL_ANSWER_CHECK(12,"Trenutna provjera odgovora", "provjera odgovora na pitanje odmah nakon pojedinačnog upisa odgovora"),
    P13_QUESTIONS_IN_CONTENT(13,"Prikaz pitanja u sadržaju","Prikaz pitanja u posebnom okviru kao dio materijala za učenje"),
    P14_IMMUTABLE_LEARNING_STYLE_VALUES(14,"Perzistentne vrijednosti stilova učenja", "Rezultat testiranja ne utječe na brojčane vrijednosti koje određuju stilove učenja"),
    P15_PICTURE_ON_THE_BEGINNING(15,"Prikaz skice ili slike na početku", "Postoji li skica ili slika vezana za materijal, prikazuje se na početku"),
    P16_AUDIO_OR_VIDEO_ANSWER(16,"Odgovor u obliku Audio / Video zapisa","Odgovaranje na esejska pitanja snimanjem odgovora u obliku Audio ili Video zapisa"),
    P17_MOTIVATIONAL_COMMENT(17,"Motivacijska poruka", "Kod neuspješnog prolaska testiranja prikazuje se motivacijska poruka"),
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
