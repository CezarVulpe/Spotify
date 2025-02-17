package app.user;

import lombok.Getter;

public class Notifications {
    @Getter
    private String name;
    @Getter
    private String description;
    public Notifications(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
