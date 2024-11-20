package io.github.lucianoawayand.lubank_app.Home.model;

public class Action {

    private final int icon;
    private final String title;
    private Class<?> targetActivity;


    public Action(int icon, String title, Class<?> targetActivity) {
        this.icon = icon;
        this.title = title;
        this.targetActivity = targetActivity;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getTargetActivity() {
        return targetActivity;
    }
}
