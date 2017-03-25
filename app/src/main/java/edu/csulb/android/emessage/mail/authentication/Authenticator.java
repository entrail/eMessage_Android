package edu.csulb.android.emessage.mail.authentication;

import android.util.Log;

import com.sun.mail.imap.IMAPStore;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

import edu.csulb.android.emessage.mail.provider.GmailProvider;
import edu.csulb.android.emessage.mail.provider.IMailProvider;

public final class Authenticator {
    private static final String TAG = "Authenticator";

    public static boolean canAuthenticate(String username, String password) {
        boolean authenticated = false;
        try {
            //create properties field
            IMailProvider mailProvider = new GmailProvider();
            Properties properties = mailProvider.getReceiveProperties();
            Session emailSession = Session.getDefaultInstance(properties);

            IMAPStore imapStore = (IMAPStore) emailSession.getStore("imaps");

            imapStore.connect("smtp.gmail.com", username, password);
            authenticated = true;
        } catch (NoSuchProviderException e) {
            Log.e(TAG, "could not find provider", e);
        } catch (MessagingException e) {
            Log.e(TAG, "could not login", e);
        }
        return authenticated;
    }
}
