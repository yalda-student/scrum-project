package ir.scrumproject.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ir.scrumproject.data.model.User;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  11/20/2020.
 */
@Dao
public interface UserDao {

    @Insert
    public void insertUser(User user);


    @Query("UPDATE User SET password = :newPass WHERE email=:email")
    void changePass(String email, String newPass);

    @Query("SELECT COUNT(*) FROM User WHERE email=:email")
    int registerCount(String email);

    @Query("SELECT  * from user where email = :email and password = :password ")
    User findUserByEmail(String email, String password);

    @Query("SELECT  * from user where username = :email and password = :password ")
    User findUserByUsername(String email, String password);

}
