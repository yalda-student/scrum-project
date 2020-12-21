package ir.scrumproject.activity.signIn;

import ir.scrumproject.data.model.User2;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  12/17/2020.
 */
public interface LoginListener {

    User2 getUser(String token);
}
