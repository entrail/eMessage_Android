package edu.csulb.android.emessage.mail;

import android.util.Log;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.csulb.android.emessage.mail.authentication.Credentials;
import edu.csulb.android.emessage.mail.provider.GmailProvider;
import edu.csulb.android.emessage.mail.provider.IMailProvider;
import edu.csulb.android.emessage.models.Chat;
import edu.csulb.android.emessage.models.Contact;
import edu.csulb.android.emessage.parser.SubjectParser;

public class MailSender {
    private static final String TAG = "MailSender";

    private static Session getSession(IMailProvider provider) {
        return Session.getInstance(provider.getSendProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Credentials.getEmailAddress(), Credentials.getPassword());
                    }
                });
    }

    public static boolean send(final edu.csulb.android.emessage.models.Message message, final Chat chat) {
        if(chat.recipients == null || chat.recipients.isEmpty()) {
            return false;
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final String username = Credentials.getEmailAddress();
                final String password = Credentials.getPassword();

                IMailProvider mailProvider = new GmailProvider();

                Session session = Session.getInstance(mailProvider.getSendProperties(),
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {
                    Message msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(username));

                    for (Contact recipient : chat.recipients) {
                        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.email));
                    }
                    msg.setSubject(SubjectParser.createSubject(chat));
                    msg.setText(message.textMessage);

                    Transport.send(msg);
                } catch (MessagingException e) {
                    Log.e(TAG, "Could not sent the mail " + chat.subject, e);
                }
            }
        });
        t.start();
        try {
            //waits for the thread to finish task
            t.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "could not sent mail", e);
        }
        return true;
    }
}
