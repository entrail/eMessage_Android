package edu.csulb.android.emessage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.csulb.android.emessage.database.ChatDatabaseHelper;
import edu.csulb.android.emessage.models.Chat;
import edu.csulb.android.emessage.observables.ChatObservable;

public class ChatsOverviewFragment extends ListFragment implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        Observer{

    public static final String EXTRA_CHAT_ID = "ChatId";
    public static final String EXTRA_CHAT_SUBJECT = "ChatSubject";

    private List<Chat> listItems;
    private ChatListAdapter adapter;
    private ChatDatabaseHelper db;

    private ChatObservable chatObservable;

    public ChatsOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = new ChatDatabaseHelper(getContext());
        listItems = db.getAllChats();
        for(Chat chat : listItems)
            Log.d("TAG", "onActivityCreated: chat subject: " + chat.subject);
        adapter = new ChatListAdapter(getContext(), R.layout.list_row_item_chat, listItems);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

        chatObservable = ChatObservable.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats_overview, container, false);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Chat chat = listItems.get(position);
        Intent intent = new Intent(getContext(), ChatWindowActivity.class);
        intent.putExtra(EXTRA_CHAT_ID, chat.id);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Delete Chat");
        builder.setMessage("Do you really want to delete this chat?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteChatById(listItems.get(position).id);
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
        Log.d("TAG", "updateList: ");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "run: ");
                listItems.clear();
                listItems.addAll(db.getAllChats());
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
    public void onResume() {
        super.onResume();
        chatObservable.addObserver(this);
    }

    @Override
    public void onPause() {
        chatObservable.deleteObserver(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        db.closeDB();
        super.onDestroy();
    }
}