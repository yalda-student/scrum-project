package ir.scrumproject.api;

import ir.scrumproject.data.model.User2;

public class Member {
    private User2 user;
    private String role;

    public User2 getUser() {
        return user;
    }

    public void setUser(User2 user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
