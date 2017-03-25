package edu.csulb.android.emessage.mail;

import android.content.Context;

/**
 * Created by Adrian on 16.11.2016.
 */

public class MailReceiver {
    
    private static final String TAG = "MailReceiver";

    /**
     * Sets a listener to listen for new incoming mails - New mails are automatically stored in the database
     *
     * @param context
     */
    public static void setMailListener(final Context context) {
        Thread t = new FetchMailThread(context, true);
        t.start();
    }

    /**
     * Loads all messages from the server and stores them in the database
     *
     * @param context
     */
    public static void loadAllMessagesFromServer(final Context context) {
        Thread t = new FetchMailThread(context, false);
        t.start();
    }
}
