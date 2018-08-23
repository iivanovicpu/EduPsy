package hr.iivanovic.psyedu.db;

import lombok.Getter;

/**
 * @author iivanovic
 * @date 27.05.17.
 */
public enum ExternalLinkType {
    ATTACHMENT(1, "Privitak"),
    URL(2, "Poveznica");

    @Getter
    public final String name;

    @Getter
    public final int id;

    ExternalLinkType(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
