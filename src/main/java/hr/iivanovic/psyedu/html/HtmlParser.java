package hr.iivanovic.psyedu.html;

import java.io.File;
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

import hr.iivanovic.psyedu.db.LearningLog;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.db.User;

/**
 * @author iivanovic
 * @date 18.09.16.
 */
public class HtmlParser {

    private Sql2oModel dbProvider = Sql2oModel.getInstance();
    private static HtmlParser instance = null;

    private HtmlParser() {
    }

    public static synchronized HtmlParser getInstance() {
        if (null == instance) {
            instance = new HtmlParser();
        }
        return instance;
    }


    public String improveDocument(File doc) {
        try {
            Document document = Jsoup.parse(doc, "UTF8", doc.getName());
            return improveDocument(document.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String improveDocument(String doc) {
        Document document = Jsoup.parse(doc, "UTF8");
        return addUniqueIdAttribute(document, "h1", "h2", "h3", "h4", "h5");
    }

    private String addUniqueIdAttribute(Document document, String... tags) {
        for (String tag : tags) {
            Elements elementsByTag = document.getElementsByTag(tag);
            elementsByTag.stream().filter(element -> element.attr("id").isEmpty()).forEach(element -> element.attr("id", tag + "_" + dbProvider.nextIdx(tag)));
        }
        return document.html();
    }

    public static List<String> getAllSubjectsLinks(String htmlDoc) {
        List<String> titles = new LinkedList<>();
        Document doc = Jsoup.parse(htmlDoc);
        Elements anchors = doc.getElementsByTag("a");
        titles.addAll(anchors.stream().map(anchor -> String.valueOf(anchor.parent())).collect(Collectors.toList()));
        return titles;
    }

    public List<TitleLink> getAllSubjectsLinks(File htmlDoc, String uri, int subjectId, User student) {
        List<TitleLink> titleLinks = new LinkedList<>();
        Document doc;
        try {
            doc = Jsoup.parse(htmlDoc, "UTF8", uri);
            Elements headingElements = getElements(doc, "h1", "h2", "h3", "h4", "h5");
            for (Element element : headingElements) {
                String title = String.valueOf(element.text());
                String id = element.attr("id");
                int statusId = 0;
                if(null != student) {
                    LearningLog learningLogStatus = dbProvider.getLearningLogStatus(student.getId(), subjectId, id);
                    statusId = null == learningLogStatus ? 0 : learningLogStatus.getStatusId();
                }
                TitleLink titleLinkElement = new TitleLink(title, id, subjectId, id.substring(0, 2), statusId);
                titleLinks.add(titleLinkElement);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titleLinks;
    }

    private Elements getElements(Document document, String... tags) {
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
