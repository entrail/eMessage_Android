package edu.csulb.android.emessage.mail.provider;

import java.util.Properties;

/**
 * Created by Adrian on 17.11.2016.
 */

public abstract class IMailProvider {
    public static final Properties authProperties = new Properties();
    public static final Properties sendProperties = new Properties();
    public static final Properties receiveProperties = new Properties();

    public Properties getAuthProperties() {
        return authProperties;
    }

    public Properties getSendProperties() {
        return sendProperties;
    }

    public Properties getReceiveProperties() {
        return receiveProperties;
    }
}
