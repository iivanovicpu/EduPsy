package hr.iivanovic.psyedu.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author iivanovic
 * @date 18.09.16.
 */
public class HtmlUtil {

    public static String anchorSubtitles(String doc){
        String s = doc.replaceAll("<a id=\".*\"></a>", "");
        String replacedH2 = s.replaceAll("(<h2>)(.*)</h2>", "<h2><a id=\"$2\"></a>$2</h2>");
        String replacedH3 = replacedH2.replaceAll("(<h3>)(.*)</h3>", "<h3><a id=\"$2\"></a>$2</h3>");
        String replacedH4 = replacedH3.replaceAll("(<h4>)(.*)</h4>", "<h4><a id=\"$2\"></a>$2</h4>");
        String replacedH5 = replacedH4.replaceAll("(<h5>)(.*)</h5>", "<h5><a id=\"$2\"></a>$2</h5>");
        return replacedH5;
    }

    public static List<String> getAllSubjectsLinks(String htmlDoc){
        List<String> titles = new LinkedList<>();
        Document doc = Jsoup.parse(htmlDoc);
        Elements anchors = doc.getElementsByTag("a");
        for (Element anchor     : anchors) {
            titles.add(String.valueOf(anchor.parent()));
        }
        return titles;
    }

    public static List<String> getAllSubjectsLinks(File htmlDoc, String uri){
        List<String> titles = new LinkedList<>();
        Document doc = null;
        try {
            doc = Jsoup.parse(htmlDoc, "UTF8", uri);
            Elements anchors = doc.getElementsByTag("a");
            for (Element anchor     : anchors) {
                titles.add(String.valueOf(anchor.parent()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titles;
    }

    public static List<String> getAllSubjectsLinksFromUrl(String url){
        List<String> titles = new LinkedList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements anchors = doc.getElementsByTag("a");
            for (Element anchor     : anchors) {
                titles.add(String.valueOf(anchor.parent()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titles;
    }
}
