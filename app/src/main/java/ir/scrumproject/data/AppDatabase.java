package ir.scrumproject.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.scrumproject.data.dao.UserDao;
import ir.scrumproject.data.model.User;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  11/20/2020.
 */
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "AppData.db";
    private static AppDatabase ourInstance;

    public AppDatabase() {
    }

    public static synchronized AppDatabase getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return ourInstance;
    }

    public abstract UserDao userDao();

}
