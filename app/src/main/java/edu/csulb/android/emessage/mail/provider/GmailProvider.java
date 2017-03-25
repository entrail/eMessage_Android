package edu.csulb.android.emessage.mail.provider;

public final class GmailProvider extends IMailProvider {

    public GmailProvider() {
        //setting authentification properties
        authProperties.put("mail.smtp.starttls.enable", "true");
        authProperties.put("mail.smtp.auth", "true");
        authProperties.put("mail.smtp.host", "smtp.gmail.com");

        //Setting send properties
        sendProperties.put("mail.smtp.starttls.enable", "true");
        sendProperties.put("mail.smtp.auth", "true");
        sendProperties.put("mail.smtp.host", "smtp.gmail.com");
        sendProperties.put("mail.smtp.port", "587");

        //setting setMailListener properties
        receiveProperties.setProperty("mail.store.protocol", "imaps");
    }
}
