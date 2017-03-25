package edu.csulb.android.emessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.csulb.android.emessage.models.Contact;

public class ContactListAdapter extends ArrayAdapter<Contact> {

    private final List<Contact> contacts;

    public ContactListAdapter(Context context, int resource, List<Contact> items) {
        super(context, resource, items);
        contacts = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_item_contact, null);

            // apply viewholder
            viewHolder = new ViewHolder();
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.textView_listRowItemContact_name);
            viewHolder.textView_email = (TextView) convertView.findViewById(R.id.textView_listRowItemContact_email);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact contact = contacts.get(position);
        viewHolder.textView_name.setText(contact.firstName + " " + contact.lastName);
        viewHolder.textView_email.setText(contact.email);

        return convertView;
    }

    // class for viewholder pattern
    static class ViewHolder {
        TextView textView_name;
        TextView textView_email;
    }
}
