package hr.iivanovic.psyedu.html;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author iivanovic
 * @date 18.09.16.
 */
public class HtmlParser {

    private static HtmlParser instance = null;

    private HtmlParser() {
    }

    public static synchronized HtmlParser getInstance() {
        if (null == instance) {
            instance = new HtmlParser();
        }
        return instance;
    }

    public static List<String> getAllSubjectsLinks(String htmlDoc) {
        List<String> titles = new LinkedList<>();
        Document doc = Jsoup.parse(htmlDoc);
        Elements anchors = doc.getElementsByTag("a");
        titles.addAll(anchors.stream().map(anchor -> String.valueOf(anchor.parent())).collect(Collectors.toList()));
        return titles;
    }

    public static Elements getElements(Document document, String... tags) {
        Elements elements = new Elements();
        Elements documentAllElements = document.getAllElements();
        for (Element documentAllElement : documentAllElements) {
            for (String tag : tags) {
                if (documentAllElement.tag().getName().equals(tag)) {
                    elements.add(documentAllElement);
                    break;
                }

            }
        }
        return elements;
    }

    public String getOneTitleContent(String filePath, String id) {
        StringBuilder content = new StringBuilder();
        try {
            byte[] encoded;
            encoded = Files.readAllBytes(Paths.get(filePath));
            String html = new String(encoded, Charset.defaultCharset());

            Document document = Jsoup.parse(html, "UTF8");
            Elements elements = document.getElementsByAttributeValue("id", id);
            if (elements.size() == 0) {
                return "document not contains any content by given id: " + id;
            }
            Element headingelement = elements.get(0);
            content.append(headingelement);
            String tag = headingelement.tag().getName();
            Element el = headingelement.nextElementSibling();
            if (null != el) {
                while (tagValue(tag) < tagValue(el.tag().getName())) {
                    content.append(el);
                    el = el.nextElementSibling();
                    if (null == el)
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private static int tagValue(String tag) {
        return tag.endsWith("1") ? 1 : tag.endsWith("2") ? 2 : tag.endsWith("3") ? 3 : tag.endsWith("4") ? 4 : tag.endsWith("5") ? 5 : 9;
    }

}
