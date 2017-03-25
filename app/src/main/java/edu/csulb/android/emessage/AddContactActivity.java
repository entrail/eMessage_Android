package edu.csulb.android.emessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.csulb.android.emessage.database.ContactDatabaseHelper;
import edu.csulb.android.emessage.models.Contact;

public class AddContactActivity extends AppCompatActivity {

    EditText editText_firstName;
    EditText editText_lastName;
    EditText editText_email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_addContact_toolbar);
        toolbar.setTitle("Add Contact");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText_email = (EditText) findViewById(R.id.editText_addContact_emailAddress);
        editText_firstName = (EditText) findViewById(R.id.editText_addContact_firstName);
        editText_lastName = (EditText) findViewById(R.id.editText_addContact_lastName);
    }

    public void button_addContact_onClick(View view) {
        ContactDatabaseHelper db = new ContactDatabaseHelper(this);
        Contact contact = createContact();
        db.addContact(contact);
        db.closeDB();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Contact createContact() {
        Contact contact = new Contact();
        contact.email = editText_email.getText().toString();
        contact.firstName = editText_firstName.getText().toString();
        contact.lastName = editText_lastName.getText().toString();
        return contact;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
