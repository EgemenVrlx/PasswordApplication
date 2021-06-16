package com.example.passwordapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements GenerateTabFragment.GenerateTabInterface, SavedPasswordAdapter.ItemClicked, ListTabFragment.FragmentToActivity, PopupMenu.OnMenuItemClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    private static String FILE_PASSWORD = "file_password";

    private ArrayList<HashMap<String, String>> allDataArrayList;
    private ArrayAdapter arrayAdapter;

    private List<String> list_title;
    private List<String> list_username;
    private List<String> list_password;
    private List<Integer> list_id;

    private ListTabFragment listTabFragment;

    private int optionMenuIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        tabAdapter = new TabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    @Override
    public void itemOnClicked(int i) {

    }

    private void copyClipboard(Context context, String password){
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("copied_password", password);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, getString(R.string.copied_clipboard), Toast.LENGTH_LONG).show();
    }

    private void onItemEdit(final String title, String username, final String password){
        final EditText editTextEditTitle;
        final EditText editTextEditUsername;
        final EditText editTextEditPassword;
        Button buttonEditCancel;
        Button buttonEditSave;
        final Dialog editPopupDialog = new Dialog(listTabFragment.requireContext());

        editPopupDialog.setContentView(R.layout.edit_popup);

        editTextEditTitle = editPopupDialog.findViewById(R.id.editTextEditTitle);
        editTextEditUsername = editPopupDialog.findViewById(R.id.editTextEditUsername);
        editTextEditPassword = editPopupDialog.findViewById(R.id.editTextEditPassword);
        buttonEditCancel = editPopupDialog.findViewById(R.id.buttonEditCancel);
        buttonEditSave = editPopupDialog.findViewById(R.id.buttonEditSave);

        editTextEditTitle.setText(title);
        editTextEditUsername.setText(username);
        editTextEditPassword.setText(password);

        //Buttons onClick
        buttonEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEditTitle.setText("");
                editTextEditUsername.setText("");
                editTextEditPassword.setText("");
                editPopupDialog.dismiss();
            }
        });
        buttonEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateList(UpdateType.UPDATE, editTextEditTitle.getText().toString(), editTextEditUsername.getText().toString(), editTextEditPassword.getText().toString(), optionMenuIndex, false);
                if (editPopupDialog.isShowing()) {
                    editTextEditTitle.setText("");
                    editTextEditUsername.setText("");
                    editTextEditPassword.setText("");

                    editPopupDialog.dismiss();
                }
            }
        });

        editPopupDialog.show();
    }

    @Override
    public void showPopup(View v, int i){
        PopupMenu popupMenu = new PopupMenu(this, v.findViewById(R.id.buttonPopupMenu));
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
        this.optionMenuIndex = i;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.itemCopy:
                copyClipboard(this, list_password.get(this.optionMenuIndex));
                return true;
            case R.id.itemEdit:
                onItemEdit(list_title.get(this.optionMenuIndex), list_username.get(this.optionMenuIndex), list_password.get(this.optionMenuIndex));
                return true;
            case R.id.itemDelete:
                updateList(UpdateType.DELETE, list_title.get(this.optionMenuIndex), list_username.get(this.optionMenuIndex), list_password.get(this.optionMenuIndex), this.optionMenuIndex, false);
                return true;
            default:
                return false;
        }
    }

    public void updateList(UpdateType type, String title, String username, String password, int index, boolean isInitial){
        this.listTabFragment = (ListTabFragment) tabAdapter.getFragment(1);
        SQLiteDBHelper database = new SQLiteDBHelper(this);

        switch (type){
            case INITIAL:
                this.allDataArrayList = database.getAllData();
                this.list_title = new ArrayList<>();
                this.list_username = new ArrayList<>();
                this.list_password = new ArrayList<>();
                this.list_id = new ArrayList<>();

                if (allDataArrayList.size() == 0) {
//                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_LONG).show();
                }
                else {
                    for (int i = 0; i < allDataArrayList.size(); i++) {
                        this.list_title.add(allDataArrayList.get(i).get("title"));
                        this.list_username.add(allDataArrayList.get(i).get("username"));
                        this.list_password.add(allDataArrayList.get(i).get("password"));
                        this.list_id.add(Integer.parseInt(Objects.requireNonNull(allDataArrayList.get(i).get("id"))));
                    }

                    for (int i = 0; i < allDataArrayList.size(); i++) {
                        updateList(UpdateType.ADD, list_title.get(i), list_username.get(i), list_password.get(i), 0, true);
                    }
                }
                break;
            case ADD:
                if (this.listTabFragment != null) {
                    if (isInitial)
                        this.listTabFragment.addSavedPasswordList(title, username, password);

                    if(!isInitial){
                        if(database.isExist(title, username, password))
                            Toast.makeText(this, getString(R.string.data_already_exist), Toast.LENGTH_LONG).show();
                        else{
                            database.saveToDB(title, username, password);
                            this.listTabFragment.addSavedPasswordList(title, username, password);

                            this.allDataArrayList = database.getAllData();
                            this.list_title.add(title);
                            this.list_username.add(username);
                            this.list_password.add(password);
                            this.list_id.add(Integer.parseInt(Objects.requireNonNull(allDataArrayList.get(allDataArrayList.size() - 1).get("id"))));
                        }
                    }
                }
                else {
                    System.err.println("Fragment 2 is not initialized");
                }
                break;
            case UPDATE:
                if(this.listTabFragment != null){
                    if(database.isExist(title, username, password))
                        Toast.makeText(this, getString(R.string.data_already_exist), Toast.LENGTH_LONG).show();
                    else{
                        database.updateList(title, username, password, list_id.get(index));
                        this.listTabFragment.updateSavedPasswordList(title, username, password, index);

                        this.list_title.set(index, title);
                        this.list_username.set(index, username);
                        this.list_password.set(index, password);
                        this.list_id.set(index, index);
                    }
                }
                else
                    System.err.println("This fragment has not initialized yet");
                break;
            case DELETE:
                if(this.listTabFragment != null){
                    final String tempTitle = this.list_title.get(index),
                            tempUsername = this.list_username.get(index),
                            tempPassword = this.list_password.get(index);
                    final int tempId = this.list_id.get(index);

                    database.deleteFromDB(title, username, password);
                    this.listTabFragment.deleteSavedPasswordList(index);

                    this.list_title.remove(index);
                    this.list_username.remove(index);
                    this.list_password.remove(index);
                    this.list_id.remove(index);

                    Snackbar snackbar = Snackbar.make(this.viewPager, getString(R.string.password_deleted), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo_delete), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    updateList(MainActivity.UpdateType.ADD, tempTitle, tempUsername, tempPassword, tempId, false);
                                }
                            });
                    snackbar.show();
                }
                else
                    System.err.println("This fragment has not initialized");
                break;
            default:
                break;
        }

    }

    enum UpdateType{
        INITIAL,
        ADD,
        UPDATE,
        DELETE
    }
}