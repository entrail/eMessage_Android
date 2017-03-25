package edu.csulb.android.emessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.csulb.android.emessage.mail.MailReceiver;

public class ReceiveMailsActivity extends AppCompatActivity {
    private static final String TAG = "ReceiveMailsActivity";

    ListView listView_chats;

    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_mails);

        listView_chats = (ListView) findViewById(R.id.listView_receiveMails_chats);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItems);

        listView_chats.setAdapter(adapter);

        MailReceiver.loadAllMessagesFromServer(this);
        // start listener to listen for new mails.
        // new mails will be printed out in the log console
        MailReceiver.setMailListener(this);
    }

    int counter = 1;

    public void button_receiveMails_request_onClick(View view) {
        /*
        listItems.add(0, Integer.toString(counter++));
        adapter.notifyDataSetChanged();
        */
        MailReceiver.loadAllMessagesFromServer(this);
    }

    // TODO: implement received mail callback which adds message subject to list
}
