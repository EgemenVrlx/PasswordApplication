package com.example.passwordapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class GenerateTabFragment extends Fragment {

    private GenerateTabInterface mainActivity;

    private CheckBox checkBoxLower;
    private CheckBox checkBoxUpper;
    private CheckBox checkBoxNumbers;
    private CheckBox checkBoxSymbols;

    private TextView textViewPassword;

    private EditText editTextLength;
    private EditText editTextTitle;
    private EditText editTextUsername;

    private Button buttonMinus;
    private Button buttonPlus;
    private Button buttonGenerate;
    private Button buttonCancel;
    private Button buttonSave;

    private LinearLayout savePasswordLayout;

    private int leng;

    private String title;
    private String username;
    private String pass;

    public interface GenerateTabInterface{
        void updateList(MainActivity.UpdateType type, String title, String username, String password, int index, boolean isInitial);
    }

    public GenerateTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mainActivity = (GenerateTabInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View generateTabView = inflater.inflate(R.layout.fragment_generate_tab, container, false);

        init(generateTabView);

        this.leng = Integer.parseInt(editTextLength.getText().toString());

        onClicks();

        return generateTabView;
    }

    private void init(View view) {

        this.checkBoxLower = view.findViewById(R.id.checkBoxLower);
        this.checkBoxUpper = view.findViewById(R.id.checkBoxUpper);
        this.checkBoxNumbers = view.findViewById(R.id.checkBoxNumbers);
        this.checkBoxSymbols = view.findViewById(R.id.checkBoxSymbols);

        this.editTextLength = view.findViewById(R.id.editTextLength);
            this.editTextLength.setText("8");

        this.buttonMinus = view.findViewById(R.id.buttonMinus);
        this.buttonPlus = view.findViewById(R.id.buttonPlus);
        this.buttonGenerate = view.findViewById(R.id.buttonGenerate);

        this.editTextTitle = view.findViewById(R.id.editTextTitle);
        this.editTextUsername = view.findViewById(R.id.editTextUsername);

        this.textViewPassword = view.findViewById(R.id.textViewPassword);

        this.buttonCancel = view.findViewById(R.id.buttonCancel);
        this.buttonSave= view.findViewById(R.id.buttonSave);

        this.savePasswordLayout = view.findViewById(R.id.savePasswordLayout);
        this.savePasswordLayout.setVisibility(View.GONE);
    }

    private void setLayoutVisibility(Boolean isVisible){
        if(isVisible){
            this.savePasswordLayout.setVisibility(View.GONE);
        }
        else{
            this.savePasswordLayout.setVisibility(View.VISIBLE);
        }
    }

    private void onClicks() {
        this.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {
                    String lengS = editTextLength.getText().toString();
                    if (lengS.isEmpty()) {
                        lengS = "0";
                        leng = Integer.parseInt(lengS);
                    }
                    else{
                        leng = Integer.parseInt(editTextLength.getText().toString());
                    }
                }

                leng--;
                if(leng <= 0) {
                    Toast.makeText(view.getContext(), getString(R.string.small_or_equal_zero), Toast.LENGTH_SHORT).show();
                    leng = 1;
                }
                editTextLength.setText(String.valueOf(leng));
            }
        });

        this.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    String lengS = editTextLength.getText().toString();
                    if (lengS.isEmpty()) {
                        lengS = "0";
                        leng = Integer.parseInt(lengS);
                    }
                    else{
                        leng = Integer.parseInt(editTextLength.getText().toString());
                    }
                }

                leng++;
                if(leng <= 0) {
                    Toast.makeText(view.getContext(), getString(R.string.small_or_equal_zero), Toast.LENGTH_SHORT).show();
                    leng = 1;
                }
                editTextLength.setText(String.valueOf(leng));
            }
        });

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkCheckBox;
                boolean checkLeng;

                String lengS = editTextLength.getText().toString();
                if (lengS.length() >= 8) {
                    Toast.makeText(view.getContext(), getString(R.string.seven_digits_bug), Toast.LENGTH_SHORT).show();
                }
                else {
                    {
                        if (lengS.isEmpty()) {
                            lengS = "1";
                            leng = Integer.parseInt(lengS);
                            editTextLength.setText(String.valueOf(leng));
                        } else {
                            leng = Integer.parseInt(editTextLength.getText().toString());
                        }
                    }

                    if (!(checkBoxLower.isChecked() || checkBoxUpper.isChecked() || checkBoxNumbers.isChecked() || checkBoxSymbols.isChecked())) {
                        Toast.makeText(view.getContext(), getString(R.string.select_option), Toast.LENGTH_SHORT).show();
                        checkCheckBox = false;
                    } else
                        checkCheckBox = true;

                    if (leng <= 0) {
                        Toast.makeText(view.getContext(), getString(R.string.small_or_equal_zero), Toast.LENGTH_SHORT).show();
                        leng = 1;
                        editTextLength.setText(String.valueOf(leng));
                        checkLeng = false;
                    } else
                        checkLeng = true;

                    if (checkCheckBox && checkLeng) {
                        pass = new PasswordGenerator(checkBoxLower.isChecked(), checkBoxUpper.isChecked(), checkBoxNumbers.isChecked(), checkBoxSymbols.isChecked(), leng).getPassword();
                        textViewPassword.setText(pass);
                        setLayoutVisibility(false);
                    }
                }
            }
        });

        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextTitle.setText("");
                editTextUsername.setText("");

                setLayoutVisibility(true);
            }
        });

        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = editTextTitle.getText().toString();
                username = editTextUsername.getText().toString();

                mainActivity.updateList(MainActivity.UpdateType.ADD, title, username, pass, 0, false);

                editTextTitle.setText("");
                editTextUsername.setText("");

                setLayoutVisibility(true);

            }
        });
    }
}
