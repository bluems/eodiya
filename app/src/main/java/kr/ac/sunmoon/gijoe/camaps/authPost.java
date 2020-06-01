package kr.ac.sunmoon.gijoe.camaps;

import retrofit2.http.POST;

public class authPost {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private Integer id;
    private String username;
    private String nickname;
    private String accessToken;

    public authPost(Integer id, String username, String nickname, String accessToken) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.accessToken = accessToken;
    }


}
