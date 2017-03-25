package edu.csulb.android.emessage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.emessage.mail.MailReceiver;
import edu.csulb.android.emessage.mail.authentication.Credentials;
import edu.csulb.android.emessage.observables.ChatObservable;

public class MainActivity extends AppCompatActivity {

    private static final String TAB_TEXT_FAVORITES = "FAVS";
    private static final String TAB_TEXT_CHATS = "CHATS";
    private static final String TAB_TEXT_CONTACTS = "CONTACTS";

    private static final int CREATE_CONTACT_REQUEST = 1;
    private static final int CREATE_CHAT_REQUEST = 2;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ContactsFragment contactsFragment = new ContactsFragment();
    private FavoriteContactsFragment favoriteContactsFragment = new FavoriteContactsFragment();
    private ChatsOverviewFragment chatsOverviewFragment = new ChatsOverviewFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load all messages from Server
        MailReceiver.loadAllMessagesFromServer(this);
        //Set listener for new incoming mails
        MailReceiver.setMailListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main_toolbar);
        setSupportActionBar(toolbar);

        // display back arrow in menu bar
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager_main_viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_main_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int selectedTabPosition = tabLayout.getSelectedTabPosition();
        TabLayout.Tab selectedTab = tabLayout.getTabAt(selectedTabPosition);

        switch (selectedTab.getText().toString()) {
            case TAB_TEXT_CHATS:
                menu.getItem(0).setVisible(true); // write message
                menu.getItem(1).setVisible(false); // add contact
                chatsOverviewFragment.updateList();
                break;
            case TAB_TEXT_CONTACTS:
                menu.getItem(0).setVisible(false); // write message
                menu.getItem(1).setVisible(true); // add contact
                break;
            case TAB_TEXT_FAVORITES:
                menu.getItem(0).setVisible(false); // write message
                menu.getItem(1).setVisible(false); // add contact
                break;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(favoriteContactsFragment, TAB_TEXT_FAVORITES);
        adapter.addFragment(chatsOverviewFragment, TAB_TEXT_CHATS);
        adapter.addFragment(contactsFragment, TAB_TEXT_CONTACTS);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_writeMessage:
                Intent createChatIntent = new Intent(this, CreateChatActivity.class);
                startActivityForResult(createChatIntent, CREATE_CHAT_REQUEST);
                return true;
            case R.id.action_addContact:
                Intent addContactIntent = new Intent(this, AddContactActivity.class);
                startActivityForResult(addContactIntent, CREATE_CONTACT_REQUEST);
                return true;
            case R.id.action_signOut:
                Credentials.clearPassword();
                Intent loginActivityIntent = new Intent(this, LoginActivity.class);
                startActivity(loginActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CREATE_CONTACT_REQUEST:
                if (resultCode == RESULT_OK) {
                    contactsFragment.updateList();
                }
                break;
            case CREATE_CHAT_REQUEST:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Chat was created", Toast.LENGTH_SHORT).show();
                    chatsOverviewFragment.updateList();
                }
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
