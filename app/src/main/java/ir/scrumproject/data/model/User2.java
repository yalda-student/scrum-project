package ir.scrumproject.data.model;

import androidx.room.ColumnInfo;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  12/17/2020.
 */
public class User2 {

    private String id;
    private String email;
    private String name;
    private String username;
    private String password;
    private String avatar;

    public User2() {
    }

    public User2(String id, String email, String name, String username,  String avatar) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.username = username;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User2{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
