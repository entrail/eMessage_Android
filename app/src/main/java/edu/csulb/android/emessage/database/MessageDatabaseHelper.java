package edu.csulb.android.emessage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.emessage.models.Message;
import edu.csulb.android.emessage.observables.MessageObservable;

public class MessageDatabaseHelper extends DatabaseHelper {

    private MessageObservable messageObservable;

    public MessageDatabaseHelper(Context context) {
        super(context);
        messageObservable = MessageObservable.getInstance();
    }

    public boolean addMessage(Message message) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Message.COLUMN_NAME_SENDER, message.sender);
        values.put(DatabaseContract.Message.COLUMN_NAME_TEXT_MESSAGE, message.textMessage);
        values.put(DatabaseContract.Message.COLUMN_NAME_CHAT_ID_FK, message.chatId);
        values.put(DatabaseContract.Message.COLUMN_NAME_DATE_SENT, message.dateSent);
        values.put(DatabaseContract.Message.COLUMN_NAME_DATE_RECEIVED, message.dateReceived);
        values.put(DatabaseContract.Message.COLUMN_NAME_UID, message.uniqueID);
        long id = db.insert(DatabaseContract.Message.TABLE_NAME, null, values);


        if (id != -1) {
            Log.d("TAG", "addMessage: inserted");
            messageObservable.notifyObservers();
        }else {
            Log.d("TAG", "addMessage: not inserted");
        }

        return (id != -1);
    }

    public void deleteAllMessagesByChatId(String id) {
        SQLiteDatabase db = getWritableDatabase();

        String table = DatabaseContract.Message.TABLE_NAME;
        String whereClause = DatabaseContract.Message.COLUMN_NAME_CHAT_ID_FK + " = ?";
        String[] whereArgs = { id };

        db.delete(table, whereClause, whereArgs);
    }

    public List<Message> getMessagesByChatId(String chatId) {
        SQLiteDatabase db = getReadableDatabase();

        String orderBy = DatabaseContract.Message.COLUMN_NAME_DATE_RECEIVED + " ASC";
        String selection = DatabaseContract.Message.COLUMN_NAME_CHAT_ID_FK + " = ?";
        String[] selectionArgs = {chatId};

        Cursor cursor = db.query(
                DatabaseContract.Message.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null, null,
                null);

        List<Message> messages = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                messages.add(convertToMessage(cursor));
            } while (cursor.moveToNext());
        }

        return messages;
    }

    private Message convertToMessage(Cursor cursor) {
        Message message = new Message();
        message.sender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Message.COLUMN_NAME_SENDER));
        message.textMessage = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Message.COLUMN_NAME_TEXT_MESSAGE));
        message.dateSent = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Message.COLUMN_NAME_DATE_SENT));
        message.dateReceived = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Message.COLUMN_NAME_DATE_RECEIVED));
        return message;
    }
}
