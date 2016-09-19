import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hr.iivanovic.psyedu.util.HtmlUtil;

/**
 * @author iivanovic
 * @date 18.09.16.
 */
public class HtmlUtilTest {

    private String         html = "<p>&nbsp;</p>\n" +
            "<p>EduPsy - projekt</p>\n" +
            "<h1>LCMS sustav za učenje EduPsy</h1>\n" +
            "<p>Sustav treba imati dva (ili tri) nivoa korisnika: Teacher, Student (i eventualno Admin). Teacher-a po predmetu je do 3, studenata do 200 (i administratora: 1)</p>\n" +
            "<h2><a id=\"Sloj podataka\"></a>Sloj podataka</h2>\n" +
            "<p>Struktura podataka za učenje organizirana je na pet nivoa. Prvi, osnovni i temeljni je 'Odlomak'. Vi&scaron;e odlomaka čine 'Nastavnu česticu'. Vi&scaron;e nastavnih čestica čine 'Nastavnu jedinicu'. Vi&scaron;e nastavnih jedinica čini je 'Nastavnu cjelina', dok sve nastavne cjeline čine 'Predmet'. <br /> ... todo: nastaviti</p>\n" +
            "<h2><a id=\"Upravljanje sadržajem učenja\"></a>Upravljanje sadržajem učenja</h2>\n" +
            "<p>Treba voditi računa da Teacher nema posebnih informatičkih znanja i/ili vje&scaron;tina. S informatičkog aspekta, Teacher treba znati:</p>\n" +
            "<ol>\n" +
            "<li>a) pisati ili kopirati tekst u uređivaču teksta (editor) koji se nalazi u sustavu EduPsy i</li>\n" +
            "<li>b) pronaći putanju do datoteke koja sadrži sliku, audio, video ili do neke druge datoteke koju želi staviti u sadržaj učenja.</li>\n" +
            "</ol>\n" +
            "<p><br /> ... todo: nastaviti</p>\n" +
            "<h2><a id=\"Pogled na sustav EduPsy od strane studenta\"></a>Pogled na sustav EduPsy od strane studenta</h2>\n" +
            "<p>Ukratko o pedago&scaron;ko-psiholo&scaron;kom modelu u sustav EduPsy. U njega su uklječena inteligencija, preferirani stil učenja i preferirana kombinacija boje teksta i pozadine Studenta, te pozicija teksta na ekranu nastavnog materijala.</p>\n" +
            "<h3><a id=\"O Pedago&scaron;ko-psiholo&scaron;kom modelu u sustavu EduPsy\"></a>O Pedago&scaron;ko-psiholo&scaron;kom modelu u sustavu EduPsy</h3>\n" +
            "<p>U definiranje pedago&scaron;ko-psiholo&scaron;ki model studenta koriste se dva anketna upitnika i jedno pitanje koje se odnosi o Studentovoj preferiranoj kombinaciji boja teksta i podloge kod učenja. Najbolje je da Student prigodom prvog logiranja u EduPsy sustav odmah ispuni upitnike. Međutim ako to iz bilo kojeg razloga ne želi učiniti tada to i ne mora. Sustav omogućava ispunjavanje anketnog upitnika i kasnije. Anketni upitnik o inteligenciji može se ispuniti samo jednom, dok se upitnik o preferiranom stilu učenja može ispuniti i vi&scaron;e puta.</p>\n" +
            "<p>Ako korisnik ne želi ispuniti anketni upitnik tada sustav sam &bdquo;uči&ldquo; o preferiranom načinu učenja studenta.</p>\n" +
            "<h3><a id=\"Inteligencija studenta i sustav EduPsy\"></a>Inteligencija studenta i sustav EduPsy</h3>\n" +
            "<p>U sustavu EduPsy koristi se modificirani anketni upitnik 'Kako smo pametni', čiji je dio prikazan na slici, za dobivanje rezultata o najjačoj kompomenti (Lingvističku, Logičko- matematičku i Prostornu. Tjelesno-kinestetička, Glazbena, Interpersonalna, Intrapersonalna i Prirodna se u sustavu EduPsy ne mjere) inteligencije temeljene na Gardnerovoj teoriji vi&scaron;estruke inteligencije. Nakon ispunjavanja ankete, sustav dobije tri brojčane vrijednosti: po jedna za lingvističku, logičko-matematičku i prostornu inteligenciju. Najača komponenta je ona s najvećom vrijednosti. Temeljem tih vrijednosti i opisanih nastavnih strategija u tablicama sustav EduPsy</p>\n" +
            "<h3><a id=\"Kombinacija boje teksta i podloge u sustavu EduPsy\"></a>Kombinacija boje teksta i podloge u sustavu EduPsy</h3>\n" +
            "<p>postoji nekoliko različitih kombinacija boja teksta i podloge</p>\n" +
            "<p><h4><a id=\"red\"></a>redoslijed</h4><br /> ... todo: nastaviti</p>\n" +
            "<h3><a id=\"Pozicija postavljanja sadržaja za učenje na ekranu\"></a>Pozicija postavljanja sadržaja za učenje na ekranu</h3>\n" +
            "<p><br /> ... todo: nastaviti</p>\n" +
            "<h3><a id=\"Realizacija pedago&scaron;ko-psiholo&scaron;kog modela\"></a>Realizacija pedago&scaron;ko-psiholo&scaron;kog modela</h3>\n" +
            "<p><br /> ... todo: nastaviti</p>\n" +
            "<h4><a id=\"Redoslijed primjene programskog rje&scaron;enja na nastavni sadržaj u svrhu prezentacije nastavnom materijala\"></a>Redoslijed primjene programskog rje&scaron;enja na nastavni sadržaj u svrhu prezentacije nastavnom materijala</h4>\n" +
            "<p>Po redoslijedu primjene EduPsy sustav na sadržaj primjenjuje aplikacije i predlo&scaron;ke ovim redom: <br /> ... todo: nastaviti</p>\n" +
            "<h3><a id=\"Primjena adaptivne tehnologije u sustavu EduPsy\"></a>Primjena adaptivne tehnologije u sustavu EduPsy</h3>\n" +
            "<p>Postoje različiti tipovi adaptivne tehnologije. Od elemenata adaptivnosti u adaptivnoj navigaciji (prema doljnjem prikazu) <br /> ... todo: nastaviti</p>" +
            "<h4>TEST</h4>";

    @Before
    public void setup() {
    }

    @Test
    public void testAnchoring() throws IOException {
;

        String doc = HtmlUtil.anchorSubtitles(html);
        Assert.assertTrue(doc.contains("<h4><a id=\"TEST\"></a>TEST</h4>"));
        System.out.println(doc);
    }

    @Test
    public void jsoupParsingTest(){
        List<String> allSubjectsLinks = HtmlUtil.getAllSubjectsLinks(html);
        for (String subtitle : allSubjectsLinks) {
            System.out.println(subtitle);
        }
    }

    @Test
    public void testGetOneTitle(){
        String oneTitleContent = HtmlUtil.getOneTitleContent("/home/iivanovic/edupsy/materijali/edupsy.html", "Sloj podataka");
        System.out.println(oneTitleContent);
    }

    @Test
    public void testGetTagByTitle(){
        String oneTitleContent = HtmlUtil.getHeadingTagByTitle(html, "Sloj podataka");
        System.out.println(oneTitleContent);
    }
}
