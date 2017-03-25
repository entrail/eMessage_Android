package edu.csulb.android.emessage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.emessage.models.Chat;
import edu.csulb.android.emessage.models.ChatMember;
import edu.csulb.android.emessage.models.Contact;

public class ChatMemberDatabaseHelper extends DatabaseHelper {

    public ChatMemberDatabaseHelper(Context context) {
        super(context);
    }

    public void addChatMember(Chat chat, Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ChatMember.COLUMN_NAME_CHAT_ID_FK, chat.id);
        values.put(DatabaseContract.ChatMember.COLUMN_NAME_CONTACT_EMAIL_FK, contact.email);
        db.insert(DatabaseContract.ChatMember.TABLE_NAME, null, values);
    }

    public void deleteAllChatMemberByChatId(String id) {
        SQLiteDatabase db = getWritableDatabase();

        String table = DatabaseContract.ChatMember.TABLE_NAME;
        String whereClause = DatabaseContract.ChatMember.COLUMN_NAME_CHAT_ID_FK + " = ?";
        String[] whereArgs = { id };

        db.delete(table, whereClause, whereArgs);
    }

    public List<ChatMember> getMembersByChatId(String chatId) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = DatabaseContract.ChatMember.COLUMN_NAME_CHAT_ID_FK + " = ?";
        String[] selectionArgs = {chatId};

        Cursor cursor = db.query(DatabaseContract.ChatMember.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null, null, null);

        List<ChatMember> chatMembers = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                chatMembers.add(convertToChatMember(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return chatMembers;
    }

    private ChatMember convertToChatMember(Cursor cursor) {
        ChatMember chatMember = new ChatMember();
        chatMember.chatId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChatMember.COLUMN_NAME_CHAT_ID_FK));
        chatMember.contactEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChatMember.COLUMN_NAME_CONTACT_EMAIL_FK));
        return chatMember;
    }
}
