package ir.scrumproject.retrofit.response;

import ir.scrumproject.data.model.User2;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  12/14/2020.
 */
public class LoginResponse {

    private String token;
    private User2 user;
    private String error;

    public LoginResponse(String token, User2 user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User2 getUser() {
        return user;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
