package edu.csulb.android.emessage.models;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    public String id = "";
    public String subject = "";
    public List<Message> messages = new ArrayList<>();
    public List<Contact> recipients = new ArrayList<>();
}
