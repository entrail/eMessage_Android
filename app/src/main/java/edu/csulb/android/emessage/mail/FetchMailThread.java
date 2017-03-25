package edu.csulb.android.emessage.mail;

import android.content.Context;
import android.util.Log;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.InternetAddress;

import edu.csulb.android.emessage.database.ChatDatabaseHelper;
import edu.csulb.android.emessage.database.MessageDatabaseHelper;
import edu.csulb.android.emessage.mail.authentication.Credentials;
import edu.csulb.android.emessage.mail.provider.GmailProvider;
import edu.csulb.android.emessage.mail.provider.IMailProvider;
import edu.csulb.android.emessage.models.Chat;
import edu.csulb.android.emessage.models.Contact;
import edu.csulb.android.emessage.parser.SubjectParser;

public class FetchMailThread extends Thread {
    private static final String TAG = "FetchMailThread";
    private Context context;
    private MessageDatabaseHelper messageDb;
    private ChatDatabaseHelper chatDb;
    private final boolean setListener;

    public FetchMailThread(Context context, boolean setListener) {
        this.context = context;
        this.messageDb = new MessageDatabaseHelper(context);
        this.chatDb = new ChatDatabaseHelper(context);
        this.setListener = setListener;
    }

    private IMAPFolder connectToMailFolder() {
        try {
            final String username = Credentials.getEmailAddress();
            final String password = Credentials.getPassword();
            //create properties field
            IMailProvider mailProvider = new GmailProvider();
            Properties properties = mailProvider.getReceiveProperties();
            Session emailSession = Session.getDefaultInstance(properties);

            IMAPStore imapStore = null;
            imapStore = (IMAPStore) emailSession.getStore("imaps");

            imapStore.connect("smtp.gmail.com", username, password);

            return (IMAPFolder) imapStore.getFolder("Inbox");
        } catch (NoSuchProviderException e) {
            Log.e(TAG, "could not find provider", e);
        } catch (MessagingException e) {
            Log.e(TAG, "messaging exception", e);
        }
        return null;
    }

    private void setMailListener(final IMAPFolder folder) throws MessagingException {
        folder.addMessageCountListener(new MessageCountListener() {

            public void messagesAdded(MessageCountEvent e) {
                Log.d(TAG, "messagesAdded: Message Count Event Fired");
                addMessagesToDb(e.getMessages());
            }

            public void messagesRemoved(MessageCountEvent e) {
                //do nothing
//                Log.d(TAG, "messagesRemoved: Message Removed Event fired");
            }
        });

        folder.addMessageChangedListener(new MessageChangedListener() {
            public void messageChanged(MessageChangedEvent e) {
                //do nothing
//                Log.d(TAG, "messageChanged: Message Changed Event fired");
            }
        });

        for (; ; ) {
            folder.idle(true);
        }
    }

    private void addMessagesToDb(Message[] messages) {
        try {
            for (Message m : messages) {
                Chat chat = createChat(m);
                if (chat != null) {
                    edu.csulb.android.emessage.models.Message message = createMessage(m, chat.id);
                    chatDb.addChat(chat);
                    messageDb.addMessage(message);
                }
            }
        } catch (IOException | MessagingException e) {
            //e.printStackTrace();
        }
    }

    private Chat createChat(Message message) throws MessagingException {
        // get subject and chat id
        Chat chat = SubjectParser.parseSubject(message.getSubject());
        // chat is part of application
        if (chat != null) {
            // add recipients
            for (Address address : message.getAllRecipients()) {
                chat.recipients.add(new Contact(((InternetAddress) address).toString(), "", ""));
            }
        }
        return chat;
    }

    private edu.csulb.android.emessage.models.Message createMessage(Message m, String chatId)
            throws MessagingException, IOException {
        edu.csulb.android.emessage.models.Message message = new edu.csulb.android.emessage.models.Message();
        message.chatId = chatId;
        message.uniqueID = ((IMAPFolder) m.getFolder()).getUID(m);
        message.textMessage = m.getContent().toString();
        message.dateSent = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'Z", Locale.getDefault()).format(m.getSentDate());
        message.dateReceived = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'Z", Locale.getDefault()).format(m.getReceivedDate());
        message.sender = ((InternetAddress) m.getFrom()[0]).getAddress();

        return message;
    }

    public void run() {
        try {
            IMAPFolder folder = this.connectToMailFolder();
            if (folder != null) {
                folder.open(IMAPFolder.READ_WRITE);
            } else {
                Log.e(TAG, "Could not open folder!");
                return;
            }
            if (setListener) {
                this.setMailListener(folder);
            } else {
                this.addMessagesToDb(folder.getMessages());
                chatDb.closeDB();
                messageDb.closeDB();
            }
        } catch (MessagingException e) {
            Log.e(TAG, "messaging exception");
        }
    }
}
