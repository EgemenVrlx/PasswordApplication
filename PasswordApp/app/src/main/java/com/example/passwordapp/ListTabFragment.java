package com.example.passwordapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListTabFragment extends Fragment {
    private FragmentToActivity mainActivity;

    private FragmentToActivity mCallback;

    private RecyclerView recyclerViewList;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<SavedPassword> savedPasswordArrayList;

    private FloatingActionButton floatingActionButton;

    public interface FragmentToActivity{
        void updateList(MainActivity.UpdateType type, String title, String username, String password, int index, boolean isInitial);
    }

    public ListTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mainActivity = (FragmentToActivity) context;

        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentToActivity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listTabFragmentView = inflater.inflate(R.layout.fragment_list_tab, container, false);

        init(listTabFragmentView);

        mainActivity.updateList(MainActivity.UpdateType.INITIAL, null, null, null, 0, true);

        return listTabFragmentView;
    }

    private void init(View view){
        this.recyclerViewList = view.findViewById(R.id.recyclerViewList);
        this.recyclerViewList.setHasFixedSize(true);

        this.layoutManager = new LinearLayoutManager(getContext());
        this.recyclerViewList.setLayoutManager(layoutManager);

        this.savedPasswordArrayList = new ArrayList<SavedPassword>();

        this.myAdapter = new SavedPasswordAdapter(getContext(), this.savedPasswordArrayList);

        this.recyclerViewList.setAdapter(this.myAdapter);

        this.floatingActionButton = view.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click.
                final EditText editTextEditTitle;
                final EditText editTextEditUsername;
                final EditText editTextEditPassword;
                Button buttonEditCancel;
                Button buttonEditSave;
                final Dialog editPopupDialog = new Dialog(requireContext());

                editPopupDialog.setContentView(R.layout.edit_popup);

                editTextEditTitle = editPopupDialog.findViewById(R.id.editTextEditTitle);
                editTextEditUsername = editPopupDialog.findViewById(R.id.editTextEditUsername);
                editTextEditPassword = editPopupDialog.findViewById(R.id.editTextEditPassword);
                buttonEditCancel = editPopupDialog.findViewById(R.id.buttonEditCancel);
                buttonEditSave = editPopupDialog.findViewById(R.id.buttonEditSave);

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
                        if(!editTextEditPassword.getText().toString().equals("")){
                            mainActivity.updateList(MainActivity.UpdateType.ADD, editTextEditTitle.getText().toString(), editTextEditUsername.getText().toString(), editTextEditPassword.getText().toString(), 0, false);
                        }
                        else{
                            Toast.makeText(getContext(), getString(R.string.password_cannot_be_empty), Toast.LENGTH_LONG).show();
                        }

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
        });

        this.recyclerViewList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });

    }

    void addSavedPasswordList(String title, String username, String pass){
        this.savedPasswordArrayList.add(new SavedPassword(title, username, pass));
        this.myAdapter.notifyDataSetChanged();
    }

    void updateSavedPasswordList(String title, String username, String password, int index){
        this.savedPasswordArrayList.get(index).setTitle(title);
        this.savedPasswordArrayList.get(index).setUsername(username);
        this.savedPasswordArrayList.get(index).setPassword(password);
        this.myAdapter.notifyDataSetChanged();
    }

    void deleteSavedPasswordList(int i){
        this.savedPasswordArrayList.remove(i);
        this.myAdapter.notifyDataSetChanged();
    }

    void deleteAllSavedPasswordList(){
            savedPasswordArrayList.clear();
            this.myAdapter.notifyDataSetChanged();
    }
}
