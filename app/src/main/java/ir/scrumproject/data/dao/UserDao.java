package ir.scrumproject.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import ir.scrumproject.data.model.User;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  11/20/2020.
 */
@Dao
public interface UserDao {

    @Insert
    public void insertUser(User user);
}
