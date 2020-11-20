package ir.scrumproject.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  11/20/2020.
 */
@Entity
public class User {

    @PrimaryKey
    public String email;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String username;
    @ColumnInfo
    public String password;

}
