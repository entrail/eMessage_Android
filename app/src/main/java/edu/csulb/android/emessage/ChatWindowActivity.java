package edu.csulb.android.emessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import edu.csulb.android.emessage.database.ChatDatabaseHelper;
import edu.csulb.android.emessage.database.MessageDatabaseHelper;
import edu.csulb.android.emessage.mail.MailSender;
import edu.csulb.android.emessage.mail.authentication.Credentials;
import edu.csulb.android.emessage.models.Chat;
import edu.csulb.android.emessage.models.Message;
import edu.csulb.android.emessage.observables.MessageObservable;

public class ChatWindowActivity extends AppCompatActivity implements Observer {

    private int counter = -1;
    private EditText editText_message;
    private ListView listView_messages;

    private List<Message> messages;
    private Chat chat;
    private MessageDatabaseHelper db;
    private MessageListAdapter adapter;

    private MessageObservable messageObservable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        editText_message = (EditText) findViewById(R.id.editText_chatWindow_message);
        listView_messages = (ListView) findViewById(R.id.listView_chatWindow_messages);

        String chatId = getIntent().getStringExtra(ChatsOverviewFragment.EXTRA_CHAT_ID);
        db = new MessageDatabaseHelper(this);

        ChatDatabaseHelper chatDb = new ChatDatabaseHelper(this);
        chat = chatDb.getChatById(chatId);

        setupToolbar();

        messages = db.getMessagesByChatId(chatId);

        adapter = new MessageListAdapter(this, R.layout.list_row_item_chat_message_right, messages);
        listView_messages.setAdapter(adapter);

        messageObservable = MessageObservable.getInstance();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chatWindow_toolbar);
        toolbar.setTitle(chat.subject);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void button_chatWindow_sendMessage_onClick(View view) {
        Message message = createMessage();
        if (MailSender.send(message, chat)) {
            //db.addMessage(message);
            editText_message.getText().clear();
            messages.add(message);
            adapter.notifyDataSetChanged();
        }
    }

    private Message createMessage() {
        String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'Z", Locale.getDefault()).format(new Date());

        Message message = new Message();
        message.chatId = chat.id;
        //message.uniqueID = counter;
        //counter--;
        message.textMessage = editText_message.getText().toString();
        message.sender = Credentials.getEmailAddress();
        message.dateSent = date;
        message.dateReceived = date;
        return message;
    }

    public void updateList() {
        Log.d("TAG", "updateList: ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "run: ");
                messages.clear();
                messages.addAll(db.getMessagesByChatId(chat.id));

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void update(Observable observable, Object o) {
        Log.d("TAG", "update: ");
        updateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageObservable.addObserver(this);
    }

    @Override
    protected void onPause() {
        messageObservable.deleteObserver(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        db.closeDB();
        super.onDestroy();
    }
}
