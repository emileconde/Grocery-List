package com.example.android.conde.com.grocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.conde.com.grocerylist.Data.DatabaseHandler;
import com.example.android.conde.com.grocerylist.Model.Grocery;
import com.example.android.conde.com.grocerylist.R;
import com.example.android.conde.com.grocerylist.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private List<Grocery> mGroceryList;
    private List<Grocery> listItem;
    private DatabaseHandler db;
    /**checks if items are being added from list activity
     * to avoid automatic bypass*/
    private boolean addFromListActivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFromListActivity = true;
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                intent.putExtra("noBypass", addFromListActivity);
                startActivity(intent);
            }
        });

        mGroceryList = new ArrayList<>();
        listItem = new ArrayList<>();
        db = new DatabaseHandler(this);
        mRecyclerView = findViewById(R.id.recyclerViewID);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

        //Get items from db
        mGroceryList = db.getAllGroceries();
        for(Grocery c : mGroceryList){
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity("Qty: "+c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded("Added on: "+c.getDateItemAdded());
            listItem.add(grocery);
        }

        mRecyclerViewAdapter = new RecyclerViewAdapter(this, listItem);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.notifyDataSetChanged();

    }

}
