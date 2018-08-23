package hr.iivanovic.psyedu.controllers.view;

import hr.iivanovic.psyedu.db.ExternalLink;

/**
 * @author iivanovic
 * @date 27.05.17.
 */

public class ExternalLinkView extends ExternalLink {
    private LinkViewType linkViewType;

    public ExternalLinkView(ExternalLink externalLink) {
        super(externalLink.getId(), externalLink.getSubjectId(), externalLink.getTitle(), externalLink.getUrl(), externalLink.getLinkTypeId());
        this.linkViewType = resolveType(getUrl());
    }

    public boolean isImage() {
        return LinkViewType.IMAGE.equals(linkViewType);
    }

    public boolean isAudio() {
        return LinkViewType.AUDIO.equals(linkViewType);
    }

    public boolean isVideo() {
        return LinkViewType.VIDEO.equals(linkViewType);
    }

    private LinkViewType resolveType(String url) {
        if (url.toLowerCase().matches("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)"))
            return LinkViewType.IMAGE;
        if (url.toLowerCase().matches("([^\\s]+(\\.(?i)(mp3|wma|wav|wave))$)"))
            return LinkViewType.AUDIO;
        if (url.toLowerCase().matches("([^\\s]+(\\.(?i)(avi|asf|mpeg|mpg|mp4))$)"))
            return LinkViewType.VIDEO;
        if (url.toLowerCase().matches("([^\\s]+(\\.(?i)(doc|docx))$)"))
            return LinkViewType.DOCUMENT_DOC;
        if (url.toLowerCase().matches("([^\\s]+(\\.(?i)(xls|xlsx))$)"))
            return LinkViewType.DOCUMENT_XLS;
        if (url.toLowerCase().matches("([^\\s]+(\\.(?i)(pdf))$)"))
            return LinkViewType.DOCUMENT_PDF;
        return LinkViewType.OTHER;
    }

    public static void main(String[] args) {
        ExternalLink link = new ExternalLink(1, 1, "test", "igor.jpeg", 1);
        ExternalLink link1 = new ExternalLink(1, 1, "test", "/materijali/anatomijab/Alan-WalkerFaded.mp3", 1);
        ExternalLink link2 = new ExternalLink(1, 1, "test", "igor.asf", 1);
        ExternalLink link3 = new ExternalLink(1, 1, "test", "igor.PDF", 1);
        ExternalLinkView ev = new ExternalLinkView(link);
        ExternalLinkView ev1 = new ExternalLinkView(link1);
        ExternalLinkView ev2 = new ExternalLinkView(link2);
        ExternalLinkView ev3 = new ExternalLinkView(link3);
        System.out.println(ev.linkViewType);
        System.out.println(ev1.linkViewType);
        System.out.println(ev2.linkViewType);
        System.out.println(ev3.linkViewType);
    }
}
