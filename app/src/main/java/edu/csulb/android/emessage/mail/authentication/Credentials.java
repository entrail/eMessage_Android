package edu.csulb.android.emessage.mail.authentication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import edu.csulb.android.emessage.LoginActivity;

public final class Credentials {
    public static final String PREFS_NAME = "CredentialsPreferences";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(LoginActivity.getContextOfApplication());
    }

    public static void setEmailAddress(String email) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public static String getEmailAddress() {
        return getSharedPreferences().getString(KEY_EMAIL, "");
    }

    public static void setPassword(String password) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public static String getPassword() {
        return getSharedPreferences().getString(KEY_PASSWORD, "");
    }

    public static boolean areSet() {
        return !getEmailAddress().isEmpty() && !getPassword().isEmpty();
    }

    public static void clearCredentials() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_EMAIL, "");
        editor.putString(KEY_PASSWORD, "");
        editor.apply();
    }

    public static void clearPassword() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_PASSWORD, "");
        editor.apply();
    }
}
