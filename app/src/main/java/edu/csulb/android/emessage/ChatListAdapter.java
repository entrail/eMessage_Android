package edu.csulb.android.emessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.csulb.android.emessage.models.Chat;

public class ChatListAdapter extends ArrayAdapter<Chat> {

    private final List<Chat> chats;

    public ChatListAdapter(Context context, int resource, List<Chat> items) {
        super(context, resource, items);
        chats = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_item_chat, null);

            // apply viewholder
            viewHolder = new ViewHolder();
            viewHolder.textView_subject = (TextView) convertView.findViewById(R.id.textView_listRowItemChat_subject);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Chat chat = chats.get(position);
        viewHolder.textView_subject.setText(chat.subject);

        return convertView;
    }

    // class for viewholder pattern
    static class ViewHolder {
        TextView textView_subject;
    }
}
