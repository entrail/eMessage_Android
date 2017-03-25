package edu.csulb.android.emessage.parser;

import edu.csulb.android.emessage.models.Chat;

public class SubjectParser {
    private static final String SEPERATOR = "°°°";
    private static final String PREFIX = "eMessage";


    public static String createSubject(Chat chat) {
        return createSubject(chat.id, chat.subject);
    }

    public static String createSubject(String chatId, String subject) {
        return PREFIX + SEPERATOR + chatId + SEPERATOR + subject;
    }

    public static Chat parseSubject(String subject) {
        if (subject.startsWith(PREFIX + SEPERATOR)) {
            subject = subject.replace(PREFIX + SEPERATOR, "");
            String[] trimmed = subject.split(SEPERATOR, 2);
            if (trimmed.length == 2) {
                String chatId = trimmed[0];
                String chatSubject = trimmed[1];

                Chat chat = new Chat();
                chat.id = chatId;
                chat.subject = chatSubject;
                return chat;
            }
        }
        return null;
    }
}
