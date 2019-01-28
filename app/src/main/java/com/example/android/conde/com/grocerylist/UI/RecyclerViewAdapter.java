package com.example.android.conde.com.grocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.conde.com.grocerylist.Activities.DetailsActivity;
import com.example.android.conde.com.grocerylist.Data.DatabaseHandler;
import com.example.android.conde.com.grocerylist.Model.Grocery;
import com.example.android.conde.com.grocerylist.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Grocery> mGroceries;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog mDialog;
    private LayoutInflater mInflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceries) {
        mContext = context;
        mGroceries = groceries;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row
        , parent, false);
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Grocery grocery = mGroceries.get(position);
        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return mGroceries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView groceryItemName;
        private TextView quantity;
        private TextView dateAdded;
        private Button editButton;
        private Button deleteButton;
        //holds the id of every item in the database
        private int id;
        private ViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            groceryItemName = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            dateAdded = itemView.findViewById(R.id.dateAdded);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Go to detailsActivity
                    int position = getAdapterPosition();
                    Grocery grocery = mGroceries.get(position);
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateItemAdded());
                    mContext.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.editButton:
                    editItem(mGroceries.get(getAdapterPosition()));
                    break;
                case R.id.deleteButton:
                    deleteItem(mGroceries.get(getAdapterPosition()).getId());
                    break;
            }
        }

        public void deleteItem(final int id){
            alertDialogBuilder = new AlertDialog.Builder(mContext);
            mInflater = LayoutInflater.from(mContext);
            View view = mInflater.inflate(R.layout.confirmation_dialog, null);
            Button yesButton = view.findViewById(R.id.yesButton);
            Button noButton = view.findViewById(R.id.noButton);
            alertDialogBuilder.setView(view);
            mDialog = alertDialogBuilder.create();
            mDialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(mContext);
                    db.deleteGrocery(id);
                    mGroceries.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    mDialog.dismiss();
                }
            });

            //Leave the dialog and do nothing
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });

        }


        public void editItem(final Grocery grocery){
            alertDialogBuilder = new AlertDialog.Builder(mContext);
            mInflater = LayoutInflater.from(mContext);
            View view = mInflater.inflate(R.layout.dialog_layout, null);
            alertDialogBuilder.setView(view);
            mDialog = alertDialogBuilder.create();
            mDialog.show();

            final EditText groceryItem = view.findViewById(R.id.et_grocery_item);
            final EditText quantity = view.findViewById(R.id.et_grocery_quantity);
            final TextView title = view.findViewById(R.id.tv_dialog_title);
            Button saveButton = view.findViewById(R.id.btn_save);

            title.setText(mContext.getString(R.string.txt_edit_grocery_item));

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(mContext);
                    //update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    //Make sure those fields aren't empty
                    if(!groceryItem.getText().toString().isEmpty() &&
                            !quantity.getText().toString().isEmpty()){

                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(), grocery);
                        mDialog.dismiss();
                    }else {
                        Snackbar.make(view, "Please enter item and quantity", Snackbar.LENGTH_SHORT)
                        .show();
                    }

                }
            });

        }


    }
}
