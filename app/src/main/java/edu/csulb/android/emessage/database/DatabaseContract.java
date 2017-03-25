package edu.csulb.android.emessage.database;

import android.provider.BaseColumns;

public final class DatabaseContract {
    public static final int DATABASE_VERSION    = 1;
    public static final String DATABASE_NAME    = "eMessage.db";
    public static final String TEXT_TYPE        = " TEXT";
    public static final String INTEGER_TYPE     = " INTEGER";
    public static final String NOT_NULL         = " NOT NULL";
    public static final String COMMA_SEP        = ",";

    public static final String[] SQL_CREATE_TABLE_ARRAY = {
            User.CREATE_TABLE,
            Contact.CREATE_TABLE,
            Chat.CREATE_TABLE,
            ChatMember.CREATE_TABLE,
            Message.CREATE_TABLE
    };

    public static final String[] SQL_DELETE_TABLE_ARRAY = {
            User.DELETE_TABLE,
            Contact.DELETE_TABLE,
            Chat.DELETE_TABLE,
            ChatMember.DELETE_TABLE,
            Message.DELETE_TABLE
    };

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DatabaseContract() {}

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME               = "user";
        public static final String COLUMN_NAME_EMAIL        = "email";
        public static final String COLUMN_NAME_PASSWORD     = "password";
        public static final String COLUMN_NAME_SIGNED_IN    = "signed_in";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_NAME_EMAIL       + TEXT_TYPE     + NOT_NULL + COMMA_SEP +
                COLUMN_NAME_PASSWORD    + TEXT_TYPE     + NOT_NULL + COMMA_SEP +
                COLUMN_NAME_SIGNED_IN   + INTEGER_TYPE  + NOT_NULL + COMMA_SEP +
                "PRIMARY KEY(" + COLUMN_NAME_EMAIL + "));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Contact implements BaseColumns {
        public static final String TABLE_NAME                   = "contact";
        public static final String COLUMN_NAME_EMAIL            = "email";
        public static final String COLUMN_NAME_FIRST_NAME       = "first_name";
        public static final String COLUMN_NAME_LAST_NAME        = "last_name";
        public static final String COLUMN_NAME_USER_EMAIL_FK    = "user_email_fk";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_NAME_EMAIL           + TEXT_TYPE + NOT_NULL  + COMMA_SEP +
                COLUMN_NAME_FIRST_NAME      + TEXT_TYPE             + COMMA_SEP +
                COLUMN_NAME_LAST_NAME       + TEXT_TYPE             + COMMA_SEP +
                COLUMN_NAME_USER_EMAIL_FK   + TEXT_TYPE + NOT_NULL  + COMMA_SEP +
                "PRIMARY KEY(" + COLUMN_NAME_EMAIL + ")"            + COMMA_SEP +
                "FOREIGN KEY(" + COLUMN_NAME_USER_EMAIL_FK + ")" +
                "REFERENCES " + User.TABLE_NAME + "(" + User.COLUMN_NAME_EMAIL + "));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Chat implements BaseColumns {
        public static final String TABLE_NAME                   = "chat";
        public static final String COLUMN_NAME_ID               = "_id";
        public static final String COLUMN_NAME_SUBJECT          = "subject";
        public static final String COLUMN_NAME_USER_EMAIL_FK    = "user_email_fk";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_NAME_ID              + TEXT_TYPE + NOT_NULL  + COMMA_SEP +
                COLUMN_NAME_SUBJECT         + TEXT_TYPE + NOT_NULL  + COMMA_SEP +
                COLUMN_NAME_USER_EMAIL_FK   + TEXT_TYPE + NOT_NULL  + COMMA_SEP +
                "PRIMARY KEY(" + COLUMN_NAME_ID + ")," +
                "FOREIGN KEY(" + COLUMN_NAME_USER_EMAIL_FK + ") REFERENCES " + User.TABLE_NAME + "(" + User.COLUMN_NAME_EMAIL + "));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class ChatMember implements BaseColumns {
        public static final String TABLE_NAME                   = "chat_member";
        public static final String COLUMN_NAME_ID               = "_id";
        public static final String COLUMN_NAME_CONTACT_EMAIL_FK = "contact_email_fk";
        public static final String COLUMN_NAME_CHAT_ID_FK       = "chat_id_fk";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_NAME_ID                  + INTEGER_TYPE  + " PRIMARY KEY AUTOINCREMENT"  + COMMA_SEP +
                COLUMN_NAME_CONTACT_EMAIL_FK    + TEXT_TYPE     + NOT_NULL                      + COMMA_SEP +
                COLUMN_NAME_CHAT_ID_FK          + TEXT_TYPE     + NOT_NULL                      + COMMA_SEP +
                "FOREIGN KEY(" + COLUMN_NAME_CONTACT_EMAIL_FK + ") REFERENCES " + Contact.TABLE_NAME + "(" + Contact.COLUMN_NAME_EMAIL + ")," +
                "FOREIGN KEY(" + COLUMN_NAME_CHAT_ID_FK       + ") REFERENCES " + Chat.TABLE_NAME    + "(" + Chat.COLUMN_NAME_ID       + "));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Message implements BaseColumns {
        public static final String TABLE_NAME                   = "message";
        public static final String COLUMN_NAME_ID               = "_id";
        public static final String COLUMN_NAME_UID              = "uid";
        public static final String COLUMN_NAME_SENDER           = "sender";
        public static final String COLUMN_NAME_DATE_SENT        = "date_sent";
        public static final String COLUMN_NAME_DATE_RECEIVED    = "date_received";
        public static final String COLUMN_NAME_TEXT_MESSAGE     = "text_message";
        public static final String COLUMN_NAME_CHAT_ID_FK       = "chat_id_fk";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_NAME_ID              + INTEGER_TYPE  + " PRIMARY KEY AUTOINCREMENT"  + COMMA_SEP +
                COLUMN_NAME_UID             + INTEGER_TYPE  + NOT_NULL + " UNIQUE"          + COMMA_SEP +
                COLUMN_NAME_SENDER          + TEXT_TYPE     + NOT_NULL                      + COMMA_SEP +
                COLUMN_NAME_DATE_SENT       + TEXT_TYPE     + NOT_NULL                      + COMMA_SEP +
                COLUMN_NAME_DATE_RECEIVED   + TEXT_TYPE     + NOT_NULL                      + COMMA_SEP +
                COLUMN_NAME_TEXT_MESSAGE    + TEXT_TYPE     + NOT_NULL                      + COMMA_SEP +
                COLUMN_NAME_CHAT_ID_FK      + TEXT_TYPE     + NOT_NULL                      + COMMA_SEP +
                "FOREIGN KEY(" + COLUMN_NAME_CHAT_ID_FK + ") REFERENCES " + Chat.TABLE_NAME + "(" + Chat.COLUMN_NAME_ID + "));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
