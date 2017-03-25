package edu.csulb.android.emessage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.csulb.android.emessage.mail.authentication.Credentials;
import edu.csulb.android.emessage.models.Chat;
import edu.csulb.android.emessage.models.ChatMember;
import edu.csulb.android.emessage.models.Contact;
import edu.csulb.android.emessage.observables.ChatObservable;

public class ChatDatabaseHelper extends DatabaseHelper {

    private ChatObservable chatObservable;

    public ChatDatabaseHelper(Context context) {
        super(context);
        chatObservable = ChatObservable.getInstance();
    }

    public boolean addChat(final Chat chat) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Chat.COLUMN_NAME_ID, chat.id);
        values.put(DatabaseContract.Chat.COLUMN_NAME_SUBJECT, chat.subject);
        values.put(DatabaseContract.Chat.COLUMN_NAME_USER_EMAIL_FK, Credentials.getEmailAddress());

        long id = db.insert(DatabaseContract.Chat.TABLE_NAME, null, values);

        ChatMemberDatabaseHelper chatMemberDatabaseHelper = new ChatMemberDatabaseHelper(context);
        for (Contact recipient : chat.recipients) {
            chatMemberDatabaseHelper.addChatMember(chat, recipient);
        }

        Log.d("TAG", "addChat: ");
        if (id != -1) chatObservable.notifyObservers();

        return (id != -1);
    }

    public Chat getChatById(String chatId) {
        SQLiteDatabase db = getReadableDatabase();
        Chat chat = null;

        String[] projection = {
                DatabaseContract.Chat.COLUMN_NAME_ID,
                DatabaseContract.Chat.COLUMN_NAME_SUBJECT
        };
        String selection = DatabaseContract.Chat.COLUMN_NAME_ID + " = ? AND " +
                DatabaseContract.Chat.COLUMN_NAME_USER_EMAIL_FK + " = ?";
        String[] selectionArgs = {chatId, Credentials.getEmailAddress()};

        Cursor cursor = db.query(
                DatabaseContract.Chat.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            chat = convertToChat(cursor);
            cursor.close();

            // get chat members
            ChatMemberDatabaseHelper chatMemberDb = new ChatMemberDatabaseHelper(context);
            List<ChatMember> chatMembers = chatMemberDb.getMembersByChatId(chatId);

            // create contacts from chat members
            ContactDatabaseHelper contactDB = new ContactDatabaseHelper(context);
            for (ChatMember chatMember : chatMembers) {
                Contact contact = contactDB.getContactByEmail(chatMember.contactEmail);
                if (contact == null) contact = new Contact(chatMember.contactEmail, "", "");
                chat.recipients.add(contact);
            }

            // get chat messages
            MessageDatabaseHelper messageDB = new MessageDatabaseHelper(context);
            chat.messages = messageDB.getMessagesByChatId(chatId);
        }

        return chat;
    }

    public List<Chat> getAllChats() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                DatabaseContract.Chat.COLUMN_NAME_ID,
                DatabaseContract.Chat.COLUMN_NAME_SUBJECT
        };

        String selection = DatabaseContract.Chat.COLUMN_NAME_USER_EMAIL_FK + " = ?";
        String[] selectionArgs = { Credentials.getEmailAddress() };

        Cursor cursor = db.query(DatabaseContract.Chat.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, null);

        List<Chat> allChats = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                allChats.add(convertToChat(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return allChats;
    }

    public void deleteChatById(String id) {
        SQLiteDatabase db = getWritableDatabase();

        // delete chat member
        ChatMemberDatabaseHelper chatMemberDatabaseHelper = new ChatMemberDatabaseHelper(context);
        chatMemberDatabaseHelper.deleteAllChatMemberByChatId(id);

        // delete messages
        MessageDatabaseHelper messageDatabaseHelper = new MessageDatabaseHelper(context);
        messageDatabaseHelper.deleteAllMessagesByChatId(id);

        // delete chat
        String table = DatabaseContract.Chat.TABLE_NAME;
        String whereClause = DatabaseContract.Chat.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = { id };
        db.delete(table, whereClause, whereArgs);
    }

    private Chat convertToChat(Cursor cursor) {
        Chat chat = new Chat();
        chat.id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Chat.COLUMN_NAME_ID));
        chat.subject = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Chat.COLUMN_NAME_SUBJECT));
        return chat;
    }
}