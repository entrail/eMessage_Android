package edu.csulb.android.emessage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.emessage.mail.authentication.Credentials;
import edu.csulb.android.emessage.models.Contact;

public class ContactDatabaseHelper extends DatabaseHelper {

    public ContactDatabaseHelper(Context context) {
        super(context);
    }

    public boolean addContact(final Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Contact.COLUMN_NAME_EMAIL, contact.email);
        values.put(DatabaseContract.Contact.COLUMN_NAME_FIRST_NAME, contact.firstName);
        values.put(DatabaseContract.Contact.COLUMN_NAME_LAST_NAME, contact.lastName);
        values.put(DatabaseContract.Contact.COLUMN_NAME_USER_EMAIL_FK, Credentials.getEmailAddress());
        long id = db.insert(DatabaseContract.Contact.TABLE_NAME, null, values);

        return (id != -1);
    }

    public Contact getContactByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();

        // create empty contact
        Contact contact = null;

        // define columns which will be returned
        String[] projection = {
                DatabaseContract.Contact.COLUMN_NAME_EMAIL,
                DatabaseContract.Contact.COLUMN_NAME_FIRST_NAME,
                DatabaseContract.Contact.COLUMN_NAME_LAST_NAME
        };

        // define filter criteria
        String selection = DatabaseContract.Contact.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = { email };

        // query database
        Cursor cursor = db.query(
                DatabaseContract.Contact.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, // no grouping
                null, // no group filter
                null // no sorting
        );

        // if contact is found
        if (cursor != null && cursor.moveToFirst()) {
            contact = convertToContact(cursor);
            cursor.close();
        }

        return contact;
    }

    public List<Contact> getAllContacts() {
        SQLiteDatabase db = getReadableDatabase();

        String orderBy = String.format("%s ASC, %s ASC, %s ASC",
                DatabaseContract.Contact.COLUMN_NAME_FIRST_NAME,
                DatabaseContract.Contact.COLUMN_NAME_LAST_NAME,
                DatabaseContract.Contact.COLUMN_NAME_EMAIL);

        String selection = DatabaseContract.Contact.COLUMN_NAME_USER_EMAIL_FK + " = ?";
        String[] selectionArgs = { Credentials.getEmailAddress() };

        Cursor cursor = db.query(DatabaseContract.Contact.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null, null,
                orderBy);

        List<Contact> allContacts = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                allContacts.add(convertToContact(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return allContacts;
    }

    public void deleteContactByEmail(String email) {
        SQLiteDatabase db = getWritableDatabase();

        String table = DatabaseContract.Contact.TABLE_NAME;
        String whereClause = DatabaseContract.Contact.COLUMN_NAME_EMAIL + " = ?";
        String[] whereArgs = { email };

        db.delete(table, whereClause, whereArgs);
    }

    private Contact convertToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Contact.COLUMN_NAME_EMAIL));
        contact.firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Contact.COLUMN_NAME_FIRST_NAME));
        contact.lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Contact.COLUMN_NAME_LAST_NAME));
        return contact;
    }
}
