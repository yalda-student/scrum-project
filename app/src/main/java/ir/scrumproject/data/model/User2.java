package ir.scrumproject.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  12/17/2020.
 */
public class User2 implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User2> CREATOR = new Parcelable.Creator<User2>() {
        @Override
        public User2 createFromParcel(Parcel in) {
            return new User2(in);
        }

        @Override
        public User2[] newArray(int size) {
            return new User2[size];
        }
    };
    private String email;
    private String name;
    private String username;
    private String password;
    private String avatar;

    public User2() {
    }

    private int id;

    public User2(int id, String email, String name, String username, String avatar) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.username = username;
        this.avatar = avatar;
    }

    protected User2(Parcel in) {
        id = in.readInt();
        email = in.readString();
        name = in.readString();
        username = in.readString();
        password = in.readString();
        avatar = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(avatar);
    }
}
