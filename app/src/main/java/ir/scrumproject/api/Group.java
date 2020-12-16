package ir.scrumproject.api;

import java.util.List;

public class Group {
    private int id;
    private String name;
    private String avatar;
    private String link;
    private String bio;
    private int admin;
    private List<Member> members;
    private boolean active;
    private int max;
    private String welcomeMessage;
    private String createdAt;
    private String updatedAt;

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getBio() {
        return bio;
    }

    public String getLink() {
        return link;
    }
}
