package kr.ac.sunmoon.gijoe.camaps;

public class CommentData {
    public CommentData(String comment, int user_id, int build_id) {
        this.comment = comment;
        this.user_id = user_id;
        this.build_id = build_id;

    }

    public String getComment() {
        return comment;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getBuild_id() {
        return build_id;
    }

    private String comment;
    private int user_id;
    private int build_id;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    private String nick;

}
