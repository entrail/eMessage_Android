package edu.csulb.android.emessage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.csulb.android.emessage.database.ContactDatabaseHelper;
import edu.csulb.android.emessage.models.Contact;
import edu.csulb.android.emessage.observables.ChatObservable;

public class ContactsFragment extends ListFragment implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private static final String TAG = "ContactsFragment";

    private List<Contact> listItems;
    private ContactListAdapter adapter;
    private ContactDatabaseHelper db;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = new ContactDatabaseHelper(getContext());
        listItems = db.getAllContacts();
        adapter = new ContactListAdapter(getContext(), R.layout.list_row_item_contact, listItems);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats_overview, container, false);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: open up update contact view to update the contact
        Toast.makeText(getActivity(), "NOT IMPLEMENTED YET!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Delete Contact");
        builder.setMessage("Do you really want to delete this contact?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteContactByEmail(listItems.get(position).email);
                updateList();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }

    public void updateList() {
        listItems.clear();
        listItems.addAll(db.getAllContacts());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        db.closeDB();
        super.onDestroy();
    }
}