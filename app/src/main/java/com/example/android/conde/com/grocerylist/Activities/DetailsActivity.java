package com.example.android.conde.com.grocerylist.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.conde.com.grocerylist.R;

public class DetailsActivity extends AppCompatActivity {
    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private int groceryID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = findViewById(R.id.itemNameDetails);
        quantity = findViewById(R.id.quantity_details);
        dateAdded = findViewById(R.id.dateAdded_details);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
            groceryID  = bundle.getInt("id");
        }

    }
}
