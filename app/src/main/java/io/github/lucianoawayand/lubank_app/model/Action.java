package io.github.lucianoawayand.lubank_app.model;

public class Action {

    private final int icon;
    private final String title;

    public Action(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
