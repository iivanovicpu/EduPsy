import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author iivanovic
 * @date 15.09.16.
 */
public class ParsingHtml {

    public static void main(String[] args) throws IOException {
        File file = new File("/home/iivanovic/private/psy-edu/etc/materijali/edupsy.html");
        byte[] encoded = Files.readAllBytes(Paths.get("/home/iivanovic/private/psy-edu/etc/materijali/edupsy.html"));
        String string = new String(encoded, Charset.defaultCharset());
        String replacedCrlf = string.replaceAll("\\r\\n|\\r|\\n", "");
        System.out.println(replacedCrlf);
        String ttt = replacedCrlf.replaceFirst("(.*)(<h2>Sloj podataka</h2>.*)(<h2>.*)", "\n$2\n");
        System.out.println(ttt);
        System.getProperty("https.protocols");
    }
}
