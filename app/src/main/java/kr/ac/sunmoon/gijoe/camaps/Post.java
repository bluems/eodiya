package kr.ac.sunmoon.gijoe.camaps;

import com.google.gson.annotations.SerializedName;

public class Post {
    private String username;
    private String password;
    private String nickname;
    @SerializedName("code")
    private String text;

    public Post(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public String getText() {
        return text;
    }
}
