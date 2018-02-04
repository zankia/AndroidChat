package fr.zankia.android.chat;

public class Message {
    private String key;
    private String content;
    private String userName;
    private String userEmail;
    private Long timestamp;

    public String getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getUserEmail() {
        return userEmail;
    }


    public Message() {
        this("", "", 0L);
    }

    public Message(String content, String nickname, Long timestamp) {
        this(null, content, nickname, "", timestamp);
    }

    public Message(String content, String nickname, String email, Long timestamp) {
        this(null, content, nickname, email, timestamp);
    }

    public Message(String key, String content, String nickname, String email, Long timestamp) {
        this.key = key;
        this.content = content;
        this.userName = nickname;
        this.userEmail = email;
        this.timestamp = timestamp;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
