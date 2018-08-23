import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import hr.iivanovic.psyedu.html.HtmlParser;

/**
 * @author iivanovic
 * @date 18.09.16.
 */
public class HtmlUtilTest {

    private static HtmlParser htmlParser = HtmlParser.getInstance();

    private String content = "<p>==== content =====Živa bića su izgrađena od organskih spojeva koji sadrže ugljik. Nazivamo ih <strong>organske</strong> ili <strong>biološke molekule</strong>. Naziv organske molekule potječe od toga što se u prošlosti smatralo da ti spojevi mogu nastati samo u živom organizmu. Organske molekule su građene od različitoga broja istih ili sličnih podjedinica pa tako razlikujemo molekule s jednom podjedinicom &ndash; <strong>monomere</strong>, dvije do deset podjedinica &ndash; <strong>oligomere</strong> i s više od deset podjedinica &ndash; <strong>polimere</strong>. Ova sposobnost polimerizacije vrlo je važna za žive organizme. Najprije ona olakšava izgradnju velikih molekula. Naime, stanica ima informaciju kako sintetizirati osnovne jedinice i kako ih udružiti u veće molekule. Ako je potreban neki drugi tip molekule samo treba promijeniti informaciju o udruživanju dok se osnovne jedinice sintetiziraju na isti način. Npr. u stanicama za sintezu celuloze koristi se jedan način vezivanja molekula glukoze dok se za sintezu škroba koristi drugi. Pri tome je mehanizam nastajanja glukoze isti. <br /><br />Polimerizacija bioloških molekula također omogućuje stanici održavanje relativno jednostavnog metabolizma koji proizvodi nekoliko tipova osnovnih jedinica i koji se u osnovi ne mijenja bez obzira na potrebe stanice. Npr. dok biljka raste aminokiseline se ugrađuju u proteine neophodne za rast stabljike i listova. Nakon određenoga vremena iste aminokiseline koriste se za sintezu proteina koji su potrebni za cvijetanje. Opisani način sinteze bioloških molekula omogućuje recikliranje, pa kada neki polimer više nije potreban razgrađuje se do sastavnih dijelova koji se mogu upotrijebiti za sintezu drugih polimera. Tako stanice čuvaju energiju koja je već utrošena pri sintezi monomera. Sinteza polimera, također omogućuje da različite stanice, odnosno različiti dijelovi organizma uspješnije rade zajedno. Npr. pri razvoju mladoga embrija u sjemenci okolna tkiva ga opskrbljuju jednostavnim šećerima, aminokiselinama i lipidima. Stoga embrio može vrlo brzo sintetizirati složene molekule koje su mu potrebne. Kada bi do embria dolazile samo hranjive tvari (kisik, ugljik, dušik, fosfor i dr.) njegov razvoj bi tekao mnogo sporije. <br /><br />U prirodi se nalazi veliki broj organskih molekula zahvaljujući građi atoma ugljika koji ostvaruje čvrste kovalentne veze s drugim atomima. Organske molekule dijelimo u četiri velike skupine: </p>\n" +
            "<p>1. <strong>ugljikohidrati</strong> <br />2. <strong>proteini</strong> <br />3. <strong>lipidi</strong> <br />4. <strong>nukleinske</strong> <strong>kiseline</strong>. <br /><br />Te molekule često grade različite trodimenzionalne strukture (lanci, spirale, prsteni) o čemu ovise njihove značajke. Organske molekule imaju različite funkcionalne skupine vezane za ugljikove atome (metilnu, karboksilnu, aldehidnu, ketonsku, fosfatnu, amino i dr.). One određuju mnoga svojstva tih molekula. Velike molekule mogu imati više različitih skupina što im omogućuje da npr. istovremeno imaju svojstva kiselina i alkohola, kiselina i baza, topljivosti u lipidima u jednom području molekule i topljivosti u vodi u drugom.</p>";

    private String additionalContent = " <p>=== additionalContent ====Biljna stanica je u trenutku nastajanja i neposredno iza toga potpuno ispunjena protoplastom. Nedugo iza toga počinju se u njoj javljati tvorevine poput malenih mjehurića koji se tijekom rasta stanice međusobno spajaju i povećavaju, da bi napokon, kada stanica naraste do svoje normalne veličine, zauzele najveći dio lumena. To su vakuole. Dakle kad je stanica mlada ima više malih vakuola, a kad odraste, ima nekoliko manjih ili jednu veliku centralnu vakuolu. U tom je stadiju citoplazma potisnuta uz rub stanične stijenke i tvori citoplazmatski ovoj.</p>\n" +
            "<p>U prvo se vrijeme smatralo da su vakuole prazni prostori. Danas je poznato da vakuole nisu prazne, već ispunjene staničnim sokom. <strong>Stanični sok</strong> je vodena otopina različitih kemijskih tvari koje mogu biti korisne (npr. šećeri), štetne (npr. različite kiseline) ili indiferentne za samu stanicu. Osim tekuće faze vakuole ponekad mogu sadržavati škrobna zrnca, kristale, proteinska tjelešca i dr. Sastav staničnoga soka nije isti kod svih stanica. On ovisi o biljnoj vrsti, tipu (funkciji) stanice, te dobi i fiziološkom stanju. Vakuole su od ostatka stanice odijeljene membranom koja se naziva tonoplast.</p>\n" +
            "<p>Vakuole mogu rasti vrlo brzo jer mogu povećavati svoj volumen akumuliranjem vode s otopljenim solima. Zbog toga i biljna stanica može brzo rasti i znatno povećati svoj volumen. Nakon toga može ona kroz dulje vremensko razdoblje sintetizirati dodatne proteine, membrane i organele ili može ostati ispunjena vodom ovisno o potrebama biljke. Za razliku od toga životinjske stanice za vrijeme rasta moraju sintetizirati čitav protoplast.</p>\n" +
            "<p>Vakuole imaju važnu ulogu u reguliranju pH i koncentracije različitih iona u stanici koji se stalno izmjenjuju preko tonoplasta. One služe kao rezervoar za kraću ili dužu pohranu različitih tvari. Na kraće vrijeme pohranjuju se tvari koje stalno sudjeluju u staničnom metabolizmu kao što su aminokiseline, nitrati, fosfati i dr. Oni se vraćaju u citoplazmu u ovisnosti o potrebama stanice. U vakuolama spremišnih tkiva mnoge tvari odlažu se na duže vrijeme. Kod sjemenki mnogih vrsta u vakuolama se nalaze škrobna zrnca ili proteinska tjelešca koja imaju važnu ulogu pri klijanju sjemena. Osim toga mnogi topivi saharidi (glukoza, fruktoza, sukroza) nalaze se u velikim koncentracijama u tim tkivima (npr. šećerna repa, šećerna trska i sl.).</p>\n" +
            "<p>U vakuolama se također pohranjuju mnoge tvari koje nastaju kao sporedni produkt u procesu izmjene tvari (sekundarni metaboliti) kao što su alkaloidi, flavonoidi, tanini i dr. Neke od tih tvari su otpadni proizvodi koji su potencijalno štetni za ostatak stanice, osobito u visokim koncentracijama pa njihovo skladištenje u vakuolama predstavlja detoksifikaciju stanica. Druge su vrlo važne za biljku jer su uključene u njenu interakciju s drugim organizmima (životinjama, bakterijama ili drugim biljnim vrstama). Tako mogu služiti kao repelent za biljojede koji zbog lošega okusa ili otrovnosti neće jesti takvo lišće (npr. tisa) ili mogu imati antibakterijsko dijelovanje (npr. tanini).</p>\n" +
            "<p>Neki od tih sekundarnih metabolita različito su obojani i pomažu biljkama privući životinje radi rasprostiranja polena i sjemenki. Svi ti pigmenti u vakuolama topivi su u vodi i uglavnom pripadaju flavonoidima. Najrasprostranjenija skupina obojanih flavonoida su <strong>antocijani</strong> koji uzrokuju sve moguće nijanse obojenosti cvijetova od plave do crvene (npr. kod ruža /Rosa sp./, božura /Paeonia sp./, potočnica /Myosotis sp./ i dr.). Slično je i kod nekih plodova (npr. <em>bazge /Sambucus sp./, kaline /Ligustrum sp./ i dr.</em>). Crvenosmeđa do crna boja listova crvene bukve, crvene lijeske i sl. nastaje zajedničkim dijelovanjem klorofila u mezofilu lista i antocijana u vakuolama epiderme. Postoji mnogo različitih vrsta antocijana koji se razlikuju po obojenosti. Različiti antocijani obično dolaze zajedno i konačna boja ovisi o vrsti antocijana, njihovom omjeru smjese i ukupnoj količini. Obojenost također ovisi i o pH staničnoga soka i prisutnosti nekih iona (npr. željezo, aluminij i sl.). Osim antocijana dolaze i blijedožuti antoksantini. Kod biljaka kod koji se unutar istoga roda pojavljuju blijedožuti, plavi i crveni cvijetovi obično se radi o kombinaciji različitih antocijana i antoksantina (npr. kod <em>jaglaca /Primula sp./, jedića /Aconitum sp./ i naprstaka /Digitalis sp./</em>). Promjena boje nekih cvijetova uzrokovana je promijenom pH staničnoga soka (npr. kod <em>potočnica /Myosotis sp./ i dr.</em>).</p>\n" +
            "<p>U vakuolama su prisutni i neki neobojani flavonoidi. Neki od njih imaju istu ulogu kao i obojani jer su vidljivi nekim kukcima. Međutim najvažnija uloga tih flavonoida je zaštita od štetnoga UV zračenja koje absorbiraju. Osim u obojenim cvijetnim dijelovima oni su prisutni i kod zelenih dijelova biljke gdje isto tako štite stanične strukture uključene u fotosintezu.</p>\n" +
            "<p>Vakuole sadrže i hidrolitičke enzime pa se u njima odvija razgradnja oštećenih i/ili nepotrebnih makromolekula. U njima se odvija i razgradnja staničnih organela kada je to potrebno (npr. pri odbacivanju listova).</p>\n" +
            "<p>Biljka nema sposobnost izlučivanja štetnih tvari izvan svoga tijela, već ih sakuplja i taloži u vakuolama, a zatim, ukoliko se radi o listovima od vremena do vremena odbacuju, kad joj lišće otpada. Kad su takve tvari skupljene u drvu, gube ti dijelovi svoju fiziološku funkciju, pa služe samo kao mehanički potporanj (npr. srčika drva). Ista je situacija i u korijenu. Na temelju toga proizlazi da biljka štetne tvari paktički može izbaciti iz svoga tijela jedino posredstvom listova. Ta pojava izaziva, s druge strane, znatni utjecaj na tlo, na koje takvi listovi padaju. Ukoliko jedna biljka u svojim listovima istaloži određene količine kalcijeva karbonata, važne komponente u regulaciji plodnosti tla, tada će se odbacivanjem listova gornji slojevi tla obogatiti kalcijem, koji se najčešće putem oborinskih voda ispire iz površinskih slojeva i odlazi u dublje slojeve tla, gdje je mnogim biljkama nepristupačan. Tako mnoge drvenaste biljke (npr. obični grab, <em>Carpinus betulus</em>) imaju važnu ulogu u kruženju mineralnih tvari u tlu, u prvom redu kalcija.</p>";

    private String summary = "<p>====== sažetak =====<strong>Sažetak</strong>:</p>\n" +
            "<ul>\n" +
            "<li><strong>Botanika</strong> je dio biologije tj. znanost koja proučava sve pojave života u biljaka. Riječ &ldquo;botanika&rdquo; dolazi od grčke riječi botane što označava grmlje, travu, bilje koje raste na livadama. Za ovu znanost upotrebljava se i naziv fitologija (grč. fiton = biljka).</li>\n" +
            "</ul>\n" +
            "<p><strong>Ciljevi:</strong></p>\n" +
            "<p>&nbsp;</p>";

    @Before
    public void setup() {
    }

    @Test
    public void jsoupParsingTest() {
        List<String> allSubjectsLinks = HtmlParser.getAllSubjectsLinks(content);
        for (String subtitle : allSubjectsLinks) {
            System.out.println(subtitle);
        }
    }

    @Test
    public void regex() {
        String bodyHtml = summary + content + additionalContent;
        int characters = bodyHtml.length() / 1000;
        int segments = characters % 1000;
        int[] idxs = new int[segments];
        for (int i = 0; i < segments; i++) {
            idxs[i] = bodyHtml.substring(i * 1000, (i + 1) * 1000).lastIndexOf(32);
        }
        StringBuilder sb = new StringBuilder(bodyHtml);

        for (int idx : idxs) {
            sb.setCharAt(idx, '|');
        }
        System.out.println(sb.toString());
    }

    @Test
    public void prepareForTabs() {
        String bodyHtml = summary + content + additionalContent;
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Elements elementsByTag = document.getElementsByTag("body");
        String adoptedText = "";
        for (Element element : elementsByTag) {
            int length = element.text().length();
            System.out.println(element.nodeName() + " - " + length);
            System.out.println(length / 1000);
//            adoptedText = bodyHtml.replaceAll("(.{900,1000})\\.(\\s|</p>)", "$1._X_");
            adoptedText = bodyHtml.replaceAll("(.{900,1000})</p>", "$1</p>__X__");


        }
        System.out.println(adoptedText);
    }

    @Test
    public void newTry() {
        String tabNavigation = "<ul class=\"nav nav-tabs\"></ul>";
        String tabContent = "<div class=\"tab-content\">";
        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append(tabNavigation)
                .append(tabContent)
                .append(summary)
                .append(content)
                .append(additionalContent)
                .append("</div>");
        Document document = Jsoup.parse(bodyHtml.toString());
        Elements elements = document.getElementsByTag("p");
        String htmlBefore = elements.toString();
        System.out.println("size before: " + elements.size() + " html size: " + htmlBefore.length());
        int counter = 0;
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            Element element = elements.get(i);
            counter += element.text().length();
            System.out.println(counter);
            if (counter >= 1000) {
                Element wrap = element.wrap("<h1></h1>");
                System.out.println("-----" + wrap);
                counter = 0;
            }

        }
        String body = elements.toString();
        System.out.println("size after: " + elements.size() + " html size: " + body.length());
        System.out.println(body);

    }

    /*
    - ideja2:
        - provjeriti prema broju znakova koliko tabova će biti potrebno
        - kreirati novi prazan dokument s tab navigacijom i praznim tabovima (broj tabova koliko je izračunato u prvom koraku)
        - iterirati po elementima content-a iz baze i appendati ih na prazan dokument u pravi tab
    */
    @Test
    public void ideja2() {
        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append(summary)
                .append(content)
                .append(additionalContent);
        Document document = Jsoup.parse(bodyHtml.toString());
        StringBuilder nav = new StringBuilder();

/*
        int length = document.text().length();  // ukupno znakova
        int pages = (length / 1000) + 1;    // potrbni broj stranica
        System.out.println(length + ", pages: " + pages);
        StringBuilder tabs = new StringBuilder();
        StringBuilder nav = new StringBuilder();
        for (int i = 1; i <= pages; i++) {
            if (i == 1) {
                nav.append("<li class=\"active\"><a href=\"#").append(i).append("\" data-toggle=\"tab\">").append(i).append("</a></li>");
                tabs.append("<div class=\"tab-pane active\" id=\"").append(i).append("\"></div>");
            } else {
                nav.append("<li><a href=\"#").append(i).append("\" data-toggle=\"tab\">").append(i).append("</a></li>");
                tabs.append("<div class=\"tab-pane\" id=\"").append(i).append("\"></div>");
            }
        }
*/
/*        StringBuilder template = new StringBuilder();
        template.append("<ul class=\"nav nav-tabs\">")
                .append(nav.toString())
                .append("</ul><div class=\"tab-content\">")
                .append(tabs.toString())
                .append("</div>");
        System.out.println(template.toString());*/
        // DO OVDJE JE SVE OK

        System.out.println("--------------------");
        Elements bodyElements = document.select("body > * "); // svi elementi unutar body-ja
        List<String> tabContents = new LinkedList<>();
        StringBuilder segment = new StringBuilder();
        int tabNumber = 0;
        for (Element element : bodyElements) {
            segment.append(element); // .html()
            if (segment.length() > 1000) {
                if (tabNumber == 0) {
                    tabContents.add("<div class=\"tab-pane active\" id=\"tab" + tabNumber + "\">" + segment.toString() + "</div>");
                } else {
                    tabContents.add("<div class=\"tab-pane\" id=\"tab" + tabNumber + "\">" + segment.toString() + "</div>");
                }
                segment.delete(0, segment.length());
                tabNumber++;
            }
        }
        tabContents.add(segment.toString());


        nav.delete(0, nav.length());
        nav.append("<ul class=\"nav nav-tabs\">");
        for (int i = 0; i < tabNumber; i++) {
            if (i == 0) {
                nav.append("<li class=\"active\"><a href=\"#tab").append(i).append("\" data-toggle=\"tab\">").append(i).append("</a></li>");
            } else {
                nav.append("<li><a href=\"#tab").append(i).append("\" data-toggle=\"tab\">").append(i).append("</a></li>");
            }
        }
        nav.append("</ul>");
        StringBuilder finalContent = new StringBuilder(nav.toString());
        finalContent.append("<div class=\"tab-content\">");
        for (String tabContent : tabContents) {
//            System.out.println(" ----- tabs: " + tabNumber);
//            System.out.println(tabContent);
            finalContent.append(tabContent);
        }
        finalContent.append("</div>");
        System.out.println(finalContent.toString());

    }
}
