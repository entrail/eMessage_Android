package edu.csulb.android.emessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.csulb.android.emessage.mail.authentication.Credentials;
import edu.csulb.android.emessage.models.Message;

public class MessageListAdapter extends ArrayAdapter<Message> {

    private List<Message> messages;

    public MessageListAdapter(Context context, int resource, List<Message> items) {
        super(context, resource, items);
        messages = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = messages.get(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (message.sender.equals(Credentials.getEmailAddress())) {
            convertView = inflater.inflate(R.layout.list_row_item_chat_message_right, null);
        } else {
            convertView = inflater.inflate(R.layout.list_row_item_chat_message_left, null);
        }

        TextView textView_sender = (TextView) convertView.findViewById(R.id.textView_listRowItemChatMessage_sender);
        TextView textView_message = (TextView) convertView.findViewById(R.id.textView_listRowItemChatMessage_message);
        TextView textView_date = (TextView) convertView.findViewById(R.id.textView_listRowItemChatMessage_date);

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'Z", Locale.getDefault());
            Date date = format.parse(message.dateReceived);


            textView_sender.setText(message.sender);
            textView_message.setText(message.textMessage);
            textView_date.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(date));
        } catch (ParseException e) {
            textView_sender.setText(message.sender);
            textView_message.setText(message.textMessage);
            textView_date.setText("");
        }


        return convertView;
    }
}
