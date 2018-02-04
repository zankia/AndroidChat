package fr.zankia.android.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class UserStorage {
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";

    static void saveUserInfo(Context context, String name, String email) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(USER_NAME, name);
        editor.putString(USER_EMAIL, email);
        editor.apply();
    }

    static User getUserInfo(Context context) {
        if(!isUserLoggedIn(context)) {
            return null;
        }
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return new User(
                sharedPreferences.getString(USER_NAME, ""),
                sharedPreferences.getString(USER_EMAIL, "")
        );
    }

    static boolean isUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getString(USER_NAME, null) != null &&
                sharedPreferences.getString(USER_EMAIL, null) != null) {
            return true;
        }
        return false;
    }


    static void removeUserInfo(Context context) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(USER_NAME);
        editor.remove(USER_EMAIL);
        editor.apply();
    }
}
