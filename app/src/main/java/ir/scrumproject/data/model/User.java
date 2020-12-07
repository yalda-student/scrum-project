package ir.scrumproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
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

}
