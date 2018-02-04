package fr.zankia.android.chat;

class User {
    private String mName, mEmail;

    public User(String name, String email) {
        this.mName = name;
        this.mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }
}
