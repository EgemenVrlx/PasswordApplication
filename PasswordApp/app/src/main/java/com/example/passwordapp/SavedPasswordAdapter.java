package com.example.passwordapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SavedPasswordAdapter extends RecyclerView.Adapter<SavedPasswordAdapter.ViewHolder> {

    private ArrayList<SavedPassword> savedPasswordArrayList;

    private ItemClicked mainActivity;

    private Context context;

    public interface ItemClicked{
        void itemOnClicked(int i);
        void showPopup(View v, int i);
    }

    public SavedPasswordAdapter(Context context, ArrayList<SavedPassword> savedPasswordArrayList) {
        this.savedPasswordArrayList = savedPasswordArrayList;

        this.context = context;

        mainActivity = (ItemClicked) context;
    }

    @NonNull
    @Override
    public SavedPasswordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedPasswordAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(savedPasswordArrayList.get(position));

        holder.textViewTitle.setText(this.context.getResources().getString(R.string.title) + ": " + savedPasswordArrayList.get(position).getTitle());
        holder.textViewUsername.setText(this.context.getResources().getString(R.string.username) + ": " + savedPasswordArrayList.get(position).getUsername());
        holder.textViewPassword.setText(this.context.getResources().getString(R.string.password) + ": " + savedPasswordArrayList.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        return savedPasswordArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewUsername, textViewPassword;
        ImageButton buttonPopupMenu;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewUsername = itemView.findViewById(R.id.textViewUsername);
            this.textViewPassword = itemView.findViewById(R.id.textViewPassword);

            this.buttonPopupMenu = itemView.findViewById(R.id.buttonPopupMenu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.itemOnClicked(savedPasswordArrayList.indexOf((SavedPassword)view.getTag()));
                }
            });

            this.buttonPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.showPopup(view, savedPasswordArrayList.indexOf((SavedPassword)itemView.getTag()));
                }
            });
        }
    }
}
