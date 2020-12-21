package ir.scrumproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  11/20/2020.
 */
@Entity(indices = {@Index(value = {"email"}, unique = true), @Index(value = {"username"}, unique = true)})
public class User {

    @PrimaryKey
    @NonNull
    public String email;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String username;
    @ColumnInfo
    public String password;
    @ColumnInfo
    public String photo;

    @Ignore
    public User(String email, String name, String username, String password, String photo) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.password = password;
        this.photo = photo;
    }

    public User() {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
