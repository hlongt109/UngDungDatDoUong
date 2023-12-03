package com.longthph30891.ungdungdatdouong.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedInCustomer(String customerId) {
        editor.putString(KEY_CUSTOMER_ID, customerId);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Get the logged-in customer's ID
    public String getLoggedInCustomerId() {
        return sharedPreferences.getString(KEY_CUSTOMER_ID, "");
    }

    // Clear the session data when the customer logs out
    public void logoutCustomer() {
        editor.clear();
        editor.apply();
    }

}
