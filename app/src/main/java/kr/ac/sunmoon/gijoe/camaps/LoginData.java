package kr.ac.sunmoon.gijoe.camaps;

import java.io.Serializable;

public class LoginData implements Serializable {
    private String username;
    private String accessToken;
    private String nickname;

    LoginData(String username, String accessToken, String nickname) {
        this.username = username;
        this.accessToken = accessToken;
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getNickname() {
        return nickname;
    }
}
