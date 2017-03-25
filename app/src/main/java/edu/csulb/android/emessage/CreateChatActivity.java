package edu.csulb.android.emessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import edu.csulb.android.emessage.database.ChatDatabaseHelper;
import edu.csulb.android.emessage.database.ContactDatabaseHelper;
import edu.csulb.android.emessage.mail.authentication.Credentials;
import edu.csulb.android.emessage.models.Chat;
import edu.csulb.android.emessage.models.Contact;

public class CreateChatActivity extends AppCompatActivity {

    private EditText editText_subject;
    private AutoCompleteTextView autoCompleteTextView_recipients;

    private List<Contact> listItems = new ArrayList<>();
    private ContactListAdapter contactListAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        setupToolbar();

        /*
        ContactDatabaseHelper contactDatabaseHelper = new ContactDatabaseHelper(this);
        List<String> allContacts = new ArrayList<>();
        for (Contact contact : contactDatabaseHelper.getAllContacts()) {
            String entry = "";
            if (!contact.firstName.isEmpty()) entry.concat(contact.firstName + " ");
            if (!contact.lastName.isEmpty()) entry.concat(contact.lastName + " ");
            entry.concat("(" + contact.email + ")");
            allContacts.add(entry);
        }
        */

        editText_subject = (EditText) findViewById(R.id.editText_createChat_subject);
        autoCompleteTextView_recipients = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_createChat_contact);

        ListView listView_recipients = (ListView) findViewById(R.id.listView_createChat_recipients);
        contactListAdapter = new ContactListAdapter(this, R.layout.list_row_item_contact, listItems);
        listView_recipients.setAdapter(contactListAdapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_createChat_toolbar);
        toolbar.setTitle("Create Chat");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void button_createChat_onClick(View view) {
        ChatDatabaseHelper db = new ChatDatabaseHelper(this);
        if (db.addChat(createChat())) {
            // TODO: send message to recipients for chat invite

            db.closeDB();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(this, "Chat can not be created!", Toast.LENGTH_SHORT).show();
        }

    }

    public void button_createChat_addRecipient_onClick(View view) {
        // TODO: add validation
        listItems.add(createContact());
        autoCompleteTextView_recipients.setText("");
        contactListAdapter.notifyDataSetChanged();
    }

    private Contact createContact() {
        ContactDatabaseHelper db = new ContactDatabaseHelper(this);
        String email = autoCompleteTextView_recipients.getText().toString();
        Contact contact = db.getContactByEmail(email);
        if (contact == null) {
            contact = new Contact();
            contact.email = email;
            db.closeDB();
        }
        return contact;
    }

    private Chat createChat() {
        Chat chat = new Chat();
        chat.subject = editText_subject.getText().toString();

        for (Contact contact : listItems) {
            chat.recipients.add(contact);
        }

        chat.recipients.add(new Contact(Credentials.getEmailAddress(), "", ""));

        chat.id = String.valueOf(Objects.hash(chat.subject, chat.recipients, new Date()));
        return chat;
    }
}
