package kr.ac.sunmoon.gijoe.camaps;

import java.io.Serializable;

public class SignupData implements Serializable {
    private String id;
    private String pw;

    SignupData(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    String getPw() {
        return pw;
    }

    public String getId() {
        return id;
    }
}
